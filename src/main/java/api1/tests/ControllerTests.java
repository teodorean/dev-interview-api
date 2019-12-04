package api1.tests;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.util.List;

import api1.Buyer;
import api1.DBConnection;
import api1.Transaction;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTests {
	
	private static DBConnection dbConn;
	private static Buyer corpBuyer;
	private static Buyer individualBuyer;
	private static Transaction tr1;
	private static Transaction tr2;
	
	@BeforeClass
	public static void setUp(){
		dbConn = new DBConnection();
		corpBuyer = dbConn.createCorporate("Apple", "2514", "123 Main Rd");
		individualBuyer = dbConn.createIndividual("Ionel", "1513");
		tr1 = dbConn.createTransaction(400, "phones", corpBuyer.getId());
		tr2 = dbConn.createTransaction(250, "tablets", corpBuyer.getId());
	}
	
	@Test
	public void AexistingBuyerTest(){
		Buyer buyer = dbConn.fetchBuyers("14").get(0);
		assertTrue(buyer.getBuyerName().equals("Amazon"));
	}
	
	@Test
	public void AcreatedCorporateTest(){		
		int corpBuyerId = corpBuyer.getId();
		String corpBuyerName = dbConn.fetchBuyers(Integer.toString(corpBuyerId)).get(0).getBuyerName();
		assertTrue(corpBuyerName.equals("Apple"));
		
	}
	
	@Test
	public void AcreatedIndividualTest(){
		int indBuyerId = individualBuyer.getId();
		String indBuyerName = dbConn.fetchBuyers(Integer.toString(indBuyerId)).get(0).getBuyerName();
		assertTrue(indBuyerName.equals("Ionel"));
	}
	
	@Test
	public void BupdateCorporateAddressTest(){
		corpBuyer = dbConn.changeDetails(corpBuyer.getId(), corpBuyer.getBuyerName(), corpBuyer.getPersID(), "12 New Rd", true);
		int corpBuyerId = corpBuyer.getId();
		String corpBuyerAddress = dbConn.fetchBuyers(Integer.toString(corpBuyerId)).get(0).getAddress();
		assertTrue(corpBuyerAddress.equals("12 New Rd"));
	}
	
	@Test
	public void BupdateIndividualPersIdTest(){
		individualBuyer = dbConn.changeDetails(individualBuyer.getId(), individualBuyer.getBuyerName(), "12345", "", false);
		int indBuyerId = individualBuyer.getId();
		String indBuyerPersId = dbConn.fetchBuyers(Integer.toString(indBuyerId)).get(0).getPersID();
		assertTrue(indBuyerPersId.equals("12345"));
	}
	
	
	@Test
	public void CcreatedTransactionTest(){
		List<Transaction> tList = dbConn.fetchTransactions(Integer.toString(tr1.getId()));
		assertTrue(tList.get(0).getCustID() == (tr1.getCustID()));
	}
	
	@Test
	public void CcheckBuyerValue(){
		int corpBuyerId = corpBuyer.getId();
		Double corpBuyerValue = dbConn.fetchBuyers(Integer.toString(corpBuyerId)).get(0).getValue();
		assertTrue(corpBuyerValue == 650);
	}
	
	
	@Test
	public void ZdeleteCorporateTest(){
		boolean tf = dbConn.deleteBuyer(corpBuyer.getId(), corpBuyer.getPersID());
		System.out.println(corpBuyer.getId());
		assertTrue(tf);
	}
	
	@Test
	public void ZdeleteIndividualTest(){
		boolean tf = dbConn.deleteBuyer(individualBuyer.getId(), individualBuyer.getPersID());
		assertTrue(tf);
	}
	
	@Test
	public void ZdeleteFakeBuyerTest(){
		boolean tf = dbConn.deleteBuyer(individualBuyer.getId(), "12");
		assertFalse(tf);
	}
	

}
