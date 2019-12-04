package api1;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	DBConnection dbConn = new DBConnection();
	
	/*
	 * GET method
	 * returns all buyers
	 */
    @GetMapping("/buyer")
    public List<Buyer> allBuyers() {
        return dbConn.fetchBuyers("");
    }
    
    /*
     * GET method
     * returns all transactions
     */
    @GetMapping("/transaction")
    public List<Transaction> allTransactions(){
    	return dbConn.fetchTransactions("");
    }
    
    /*
     * GET method
     * requires buyer id as path variable
     * returns corresponding buyer
     */
    @GetMapping("/buyer/{id}")
    public Buyer oneBuyer(@PathVariable String id){
    	return dbConn.fetchBuyers(id).get(0);
    }
    
    /*
     * GET method
     * requires transaction id as path variable
     * returns corresponding transaction
     */
    @GetMapping("/transaction/{id}")
    public Transaction oneTransaction(@PathVariable String id){
    	return dbConn.fetchTransactions(id).get(0);
    }
    
    /*
     * POST method to create corporate buyer
     * requires a body of type json
     * with fields for buyer name, personal id and address
     * returns null if the buyer is not added for any reason (if buyerName already exists)
     * EX : {"name":"Samsung", "id":"1514135", "address": "1 Main Rd"}
     */
    @PostMapping("/corporate")
    public Buyer createCorporate(@RequestBody Map<String,String> body){
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	String address = body.get("address");
    	return dbConn.createCorporate(buyerName, persID, address);
    }
    
    /*
     * POST method to create individual buyer
     * requires a body of type json
     * fields for buyer name and personal id
     * returns null if the buyer is not added for any reason (if buyerName already exists)
     */
    @PostMapping("/individual")
    public Buyer createIndividual(@RequestBody Map<String,String> body){
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	//String dateReg = body.get("date");
    	return dbConn.createIndividual(buyerName, persID);
    }
    
    /*
     * POST method to create transaction
     * requires a body of type json
     * fields for buyer id, value of transaction, description
     * automatically updates buyer value
     * returns null if the transaction is not added for any reason
     */
    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Map<String,String> body){
    	int custID = Integer.parseInt(body.get("id"));
    	double value = Double.parseDouble(body.get("value"));
    	String desc = body.get("desc");
    	return dbConn.createTransaction(value, desc, custID);
    }
    
    /*
     * PUT method to update corporate buyer details
     * requires corp buyer id as path variable and body of type json
     * fields for the new buyer name, new personal id, new address
     */
    @PutMapping("/corporate/{id}")
    public Buyer updateCorporate(@PathVariable String id, @RequestBody Map<String, String> body){
    	int corpId = Integer.parseInt(id);
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	String address = body.get("address");
    	return dbConn.changeDetails(corpId, buyerName, persID, address, true);
    }
    
    /*
     * PUT method to update individual buyer details
     * requires corp buyer id as path variable and body of type json
     * fields for the new buyer name, new personal id
     */
    @PutMapping("/individual/{id}")
    public Buyer updateIndividual(@PathVariable String id, @RequestBody Map<String, String> body){
    	int indivId = Integer.parseInt(id);
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	return dbConn.changeDetails(indivId, buyerName, persID, "", false);
    }
    
    /*
     * DELETE method to delete a buyer
     * requires buyer id as path variable and body of type json
     * one field for personal id
     * automatically deletes all transactions belonging to the buyer
     */
    @DeleteMapping("/buyer/{id}")
    public boolean deleteBuyer(@PathVariable String id, @RequestBody Map<String, String> body){
    	int buyerId = Integer.parseInt(id);
    	String persID = body.get("id");
    	return dbConn.deleteBuyer(buyerId, persID);
    }
}