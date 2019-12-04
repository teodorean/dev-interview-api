package api1;

import java.util.ArrayList;
import java.util.List;

public class Buyer {
	
	private int id;
	private boolean corporate;
	private String buyerName;
	private double value;
	private String persID;
	private String dateReg;
	private String address;
	private List<Integer> transactions;
	
	public List<Integer> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Integer> transactions) {
		this.transactions = transactions;
	}

	public Buyer(){
	
	}
	
	public Buyer (int id, boolean corporate, String buyerName, double value, String persID, String dateReg, String address) {
		this.id = id;
		this.corporate = corporate;
		this.buyerName = buyerName;
		this.value = value;
		this.persID = persID;
		this.dateReg = dateReg;
		this.address = address;
	}
	
	
	
	@Override
	public String toString(){
		return "Buyer{" +
				"id=" + id +
				", buyerName ='" + buyerName + '\'' + 
				", persID='" + persID + '\'' + 
				", dateReg='" + dateReg + '\''  +
				", address='" + address + '\'' +
				"}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isCorporate() {
		return corporate;
	}

	public void setCorporate(boolean corporate) {
		this.corporate = corporate;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getPersID() {
		return persID;
	}

	public void setPersID(String persID) {
		this.persID = persID;
	}

	public String getDateReg() {
		return dateReg;
	}

	public void setDateReg(String dateReg) {
		this.dateReg = dateReg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
