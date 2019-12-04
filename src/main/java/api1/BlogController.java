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
public class BlogController {
	
	DBConnection dbConn = new DBConnection();
	
	
    @GetMapping("/buyer")
    public List<Buyer> allBuyers() {
        return dbConn.fetchBuyers("");
    }
    
    @GetMapping("/transaction")
    public List<Transaction> allTransactions(){
    	return dbConn.fetchTransactions("");
    }
    
    @GetMapping("/buyer/{id}")
    public List<Buyer> oneBuyer(@PathVariable String id){
    	return dbConn.fetchBuyers(id);
    }
    
    @GetMapping("/transaction/{id}")
    public List<Transaction> oneTransaction(@PathVariable String id){
    	return dbConn.fetchTransactions(id);
    }
    
    @PostMapping("/corporate")
    public Buyer createCorporate(@RequestBody Map<String,String> body){
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	String address = body.get("address");
    	return dbConn.createCorporate(buyerName, persID, address);
    }
    
    @PostMapping("/individual")
    public Buyer createIndividual(@RequestBody Map<String,String> body){
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	//String dateReg = body.get("date");
    	return dbConn.createIndividual(buyerName, persID);
    }
    
    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestBody Map<String,String> body){
    	int custID = Integer.parseInt(body.get("id"));
    	double value = Double.parseDouble(body.get("value"));
    	String desc = body.get("desc");
    	return dbConn.createTransaction(value, desc, custID);
    }
    
    @PutMapping("/corporate/{id}")
    public Buyer updateCorporate(@PathVariable String id, @RequestBody Map<String, String> body){
    	int corpId = Integer.parseInt(id);
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	String address = body.get("address");
    	return dbConn.changeDetails(corpId, buyerName, persID, address, true);
    }
    
    @PutMapping("/individual/{id}")
    public Buyer updateIndividual(@PathVariable String id, @RequestBody Map<String, String> body){
    	int indivId = Integer.parseInt(id);
    	String buyerName = body.get("name");
    	String persID = body.get("id");
    	return dbConn.changeDetails(indivId, buyerName, persID, "", false);
    }
    
    @DeleteMapping("/buyer/{id}")
    public boolean deleteBuyer(@PathVariable String id, @RequestBody Map<String, String> body){
    	int buyerId = Integer.parseInt(id);
    	String persID = body.get("id");
    	return dbConn.deleteBuyer(buyerId, persID);
    }
}