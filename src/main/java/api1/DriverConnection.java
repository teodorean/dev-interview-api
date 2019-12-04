package api1;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;


 

public class DriverConnection {
     
	private Connection conn;
	
	public DriverConnection(){
	
	}
	
	/**
	 * 
	 * @return the database connection
	 */
    public  Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:src/main/resources/apiDatabase.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
   
    
    public void disconnect(){
    	try {
            if (conn != null) {
                conn.close();
                System.out.println("successfully disconnected");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public  void selectAll(Connection conn){  
        String sql = "SELECT * FROM Buyer";  
          
        try {  
             
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            
              
            while (rs.next()) {  
                System.out.println(rs.getInt("id") +  "\t" +   
                                   rs.getString("buyerName") + "\t" +
                					rs.getString("persID"));  
            }  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }  
    

    
}