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
	
	public List<Buyer> fetchBuyers(String id){
		String sql = "SELECT * FROM Buyer"; 
		if (!id.isEmpty()){
			sql = sql + " WHERE id = " + id;
			System.out.println(sql);
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
	
	public List<Transaction> fetchTransactions(String id){
		String sql = "SELECT * FROM Transaction1"; 
		if (!id.isEmpty()){
			sql = sql + "WHERE id = " + id;
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
	
	public Buyer createCorporate(String buyerName, String persID, String address){
		int newID = fetchGreatest("id", "Buyer") + 1;
		Buyer newBuyer = new Buyer(newID, true, buyerName, 0, persID, "", address);
		addBuyer(newBuyer);
		return newBuyer;
	}
	
	public Buyer createIndividual(String buyerName, String persID){
		int newID = fetchGreatest("id", "Buyer") + 1;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String dateReg = dtf.format(localDate);
		Buyer newBuyer = new Buyer(newID, false, buyerName, 0, persID, dateReg, "");
		addBuyer(newBuyer);
		return newBuyer;
	}
	
	public void addBuyer(Buyer buyer){
		String sql = "INSERT INTO Buyer (id, corporate, buyerName, value, persID, dateRegistered, address) VALUES (?,?,?,?,?,?,?)";
				//buyer.getId() + ", " + buyer.isCorporate() + ", " + buyer.getBuyerName() + ", " +
				//buyer.getValue() + ", " + buyer.getPersID() + ", " + buyer.getDateReg() + ", " +
				//buyer.getAddress() + ";";
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
        } 
	}
	
	public Transaction createTransaction(double value, String desc, int custID){
		int newID = fetchGreatest("id", "Transaction1") + 1;
		int newTrID = fetchGreatest("transactionNumber", "Transaction1") + 1;
		Transaction newTr = new Transaction(newID, newTrID, value, desc, custID);
		addTransaction(newTr);
		return newTr;
	}

	public void addTransaction(Transaction newTr) {
		String sql1 = "INSERT INTO Transaction1 (id, transactionNumber, value, desc, custID)"
				+ "VALUES (?,?,?,?,?)";
		
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
        } 
		
	}

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
	
	public boolean deleteBuyer(int id, String persID){
		Buyer buyer = fetchBuyers(Integer.toString(id)).get(0);
		
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
