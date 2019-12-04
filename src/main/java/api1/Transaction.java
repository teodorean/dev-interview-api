package api1;

public class Transaction {

	private int id;
	private int transactionNumber;
	private double value;
	private String desc;
	private int custID;
	
	public Transaction(int id, int transactionNumber, double value, String desc, int custID) {
		this.id = id;
		this.transactionNumber = transactionNumber;
		this.value = value;
		this.desc = desc;
		this.custID = custID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCustID() {
		return custID;
	}

	public void setCustID(int custID) {
		this.custID = custID;
	}

}
