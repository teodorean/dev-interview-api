package api1;

import java.sql.Connection;
import java.util.List;

public class testConn {

	public testConn() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		DBConnection dbConn = new DBConnection();
		//dbConn.createCorporate("Apple", "1263151", "123 Main Rd");
		System.out.println(dbConn.fetchTransactions("246").get(0).getDesc());
	}

}
