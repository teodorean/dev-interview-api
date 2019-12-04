package api1;

import java.sql.Connection;
import java.util.List;

public class testConn {

	public testConn() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		DBConnection dbConn = new DBConnection();
		dbConn.createTransaction(1000.0, "phone", 18);
	}

}
