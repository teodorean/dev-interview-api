package api1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
	private Connection conn;
	
	public DBConnection() {
		DriverConnection dConn = new DriverConnection();
		conn = dConn.connect();
		turnOnforeignKeys();
	}
	
	/*
	 * if id is empty string, return all buyers
	 * otherwise return a list with just one buyer, the one with the corresponding id
	 */
	public List<Buyer> fetchBuyers(String id){
		String sql = "SELECT * FROM Buyer"; 
		if (!id.isEmpty()){
			sql = sql + " WHERE id = " + id;
		}
		List<Buyer> buyerList = new ArrayList<Buyer>();
        try {              
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);            
            
            while (rs.next()) {  
                Buyer buyer = new Buyer(rs.getInt("id"), rs.getBoolean("corporate"), rs.getString("buyerName"), rs.getDouble("value"), 
                		rs.getString("persID"), rs.getString("dateRegistered"), rs.getString("address"));
                List<Integer> transactionList = fetchTransactionIDs(buyer);
                buyer.setTransactions(transactionList);
                buyerList.add(buyer);
            }  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
        return buyerList;
	}
	/*
	 * return a list of all transaction numbers belonging to a buyer
	 * used to display a users list of transactions made
	 */
	public List<Integer> fetchTransactionIDs(Buyer buyer){
		int custID = buyer.getId();
		String sql = "SELECT transactionNumber FROM Transaction1 WHERE custID = " + custID; 
		List<Integer> transactionNumberList = new ArrayList<Integer>();
        try {              
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);            
            
            while (rs.next()) {  
                transactionNumberList.add(rs.getInt("transactionNumber"));
            }  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
        return transactionNumberList;
	}
	/*
	 * same principle as fetchBuyers()
	 */
	public List<Transaction> fetchTransactions(String id){
		String sql = "SELECT * FROM Transaction1"; 
		if (!id.isEmpty()){
			sql = sql + " WHERE id = " + id;
		}
		List<Transaction> transactionList = new ArrayList<Transaction>();
        try {              
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);            
            
            while (rs.next()) {  
                Transaction transaction = new Transaction(rs.getInt("id"), rs.getInt("transactionNumber"), 
                		rs.getDouble("value"), rs.getString("desc"), rs.getInt("custID"));
                transactionList.add(transaction);
            }  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
        return transactionList;
	}
	
	/*
	 * returns the greatest value of a given column from a given table
	 * used for generating new buyer ids, new transaction ids and transaction numbers
	 */
	public int fetchGreatest(String column, String table){
		String sql = "SELECT MAX(" + column + ") AS " + column + " FROM " + table;
		int greatest=0;
        try {              
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);            
            greatest = rs.getInt(column);
             
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
        return greatest;
	}
	/*
	 * creates and returns new corporate buyer
	 * calls addBuyer
	 */
	public Buyer createCorporate(String buyerName, String persID, String address){
		int newID = fetchGreatest("id", "Buyer") + 1;
		Buyer newBuyer = new Buyer(newID, true, buyerName, 0, persID, "", address);
		if(!addBuyer(newBuyer)) return null;
		return newBuyer;
	}
	/*
	 * creates and returns new indiv. buyer
	 * calls addBuyer
	 */
	public Buyer createIndividual(String buyerName, String persID){
		int newID = fetchGreatest("id", "Buyer") + 1;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String dateReg = dtf.format(localDate);
		Buyer newBuyer = new Buyer(newID, false, buyerName, 0, persID, dateReg, "");
		if(!addBuyer(newBuyer)) return null;
		return newBuyer;
	}
	/*
	 * adds a newly created buyer to the database
	 */
	public boolean addBuyer(Buyer buyer){
		String sql = "INSERT INTO Buyer (id, corporate, buyerName, value, persID, dateRegistered, address) VALUES (?,?,?,?,?,?,?)";
				//buyer.getId() + ", " + buyer.isCorporate() + ", " + buyer.getBuyerName() + ", " +
				//buyer.getValue() + ", " + buyer.getPersID() + ", " + buyer.getDateReg() + ", " +
				//buyer.getAddress() + ";";
		boolean isAdded = true;
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, buyer.getId());
			stmt.setBoolean(2, buyer.isCorporate());
			stmt.setString(3, buyer.getBuyerName());
			stmt.setDouble(4, buyer.getValue());
			stmt.setString(5, buyer.getPersID());
			stmt.setString(6, buyer.getDateReg());
			stmt.setString(7, buyer.getAddress());
			
			stmt.execute();
			
		
		} catch (SQLException e) {  
            System.out.println(e.getMessage());
            isAdded = false;
        } 
		return isAdded;
	}
	/*
	 * creates and returns new transaction
	 * calls addTransaction
	 */
	public Transaction createTransaction(double value, String desc, int custID){
		int newID = fetchGreatest("id", "Transaction1") + 1;
		int newTrID = fetchGreatest("transactionNumber", "Transaction1") + 1;
		Transaction newTr = new Transaction(newID, newTrID, value, desc, custID);
		if(!addTransaction(newTr)) return null;
		return newTr;
	}
	/*
	 * adds new transaction to database
	 * calls updateBuyerValue
	 */
	public boolean addTransaction(Transaction newTr) {
		String sql1 = "INSERT INTO Transaction1 (id, transactionNumber, value, desc, custID)"
				+ "VALUES (?,?,?,?,?)";
		boolean isAdded = true;
		
		try{
			PreparedStatement stmt = conn.prepareStatement(sql1);
			stmt.setInt(1, newTr.getId());
			stmt.setInt(2, newTr.getTransactionNumber());
			stmt.setDouble(3, newTr.getValue());
			stmt.setString(4, newTr.getDesc());
			stmt.setInt(5, newTr.getCustID());			
			stmt.execute();
			updateBuyerValue(newTr.getCustID(), newTr.getValue());
		
		} catch (SQLException e) {  
            System.out.println(e.getMessage());
            isAdded = false;
        } 
		return isAdded;
	}
	/*
	 * is called whenever a new transaction is made
	 * adds transaction value to the corresponding buyer's value
	 */
	public void updateBuyerValue(int custID, double trValue) {
		String sql1 = "SELECT value from Buyer WHERE id = " + custID;
		String sql2 = "UPDATE Buyer SET value = ? WHERE id = ?";
		try{
			PreparedStatement stmt = conn.prepareStatement(sql1);
			ResultSet rs = stmt.executeQuery();
			double currentValue = rs.getInt("value");
			stmt = conn.prepareStatement(sql2);
			stmt.setDouble(1, currentValue + trValue);
			stmt.setInt(2, custID);
			stmt.execute();
		
		} catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
	}
	
	/*
	 * updates a buyers details
	 * if buyer is corporate, it takes in a potential new address
	 * if buyer is individual, address will still remain as empty string
	 */
	public Buyer changeDetails(int id, String buyerName, String persID, String address, boolean isCorp){
		String sql = "UPDATE Buyer SET buyerName = ?, persID = ?, address = ? WHERE id = ?";
		Buyer buyer = fetchBuyers(Integer.toString(id)).get(0);
		if (buyer.isCorporate() != isCorp){
			return null;
		}
		try{
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, buyerName);
			stmt.setString(2, persID);
			stmt.setString(3, address);
			stmt.setInt(4, id);
			
			stmt.execute();
			
			buyer.setBuyerName(buyerName);
			buyer.setPersID(persID);
			buyer.setAddress(address);
		
		} catch (SQLException e) {  
            System.out.println(e.getMessage());  
        } 
		
		return buyer;
	}
	
	/*
	 * removes a buyer
	 * requires buyer id and personal ID
	 * returns true if it deletes a buyer, false otherwise
	 */
	public boolean deleteBuyer(int id, String persID){
		Buyer buyer;
		try{
			buyer = fetchBuyers(Integer.toString(id)).get(0);
		} catch (IndexOutOfBoundsException e){
			System.out.println("buyer does not exist");
			return false;
		}
		
		if (!checkPersID(id,persID)) return false;
		
		List<Integer> tList = buyer.getTransactions();
		for (int i : tList){
			deleteTransaction(i);
		}
		String sql = "DELETE from Buyer where id = ? AND persID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setString(2, persID);
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	/*
	 * check if a personal ID corresponds to the buyer id
	 * used when deleting a buyer
	 */
	public boolean checkPersID(int id, String persID){
		String sql = "SELECT id FROM Buyer WHERE id = ? and persID = ?";
		int dbID=0;
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setString(2, persID);
			ResultSet rs = stmt.executeQuery();
			dbID = rs.getInt("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return false;
		}
		if (id==dbID){
			return true;
		}
		return false;
	}
	/*
	 * deletes the transaction with the corresponding id
	 * called on all of a buyers transactions before buyer is deleted
	 */
	public boolean deleteTransaction(int id){
		String sql = "DELETE from Transaction1 where transactionNumber = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void turnOnforeignKeys(){
		try {
			conn.createStatement().execute("PRAGMA foreign_keys = ON");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
