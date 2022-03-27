package testcases;

import static org.junit.Assert.*;

import java.io.IOException; 

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import usecases.AddItem;
import usecases.Inventory;
import usecases.Payment;

public class SoftwareTest {

	private static Barcode b1;
	private static Barcode b2;
	private static Barcode b3;
	private static Barcode b4;
	private static SelfCheckoutStation testStation;
	private static Payment dummyPayment;
	private static AddItem dummyAddItem;
	private static Currency testCurrency;
	private static Card card;

	
	@BeforeClass
	public static void setUp() throws Exception 
	{
		testCurrency = Currency.getInstance(Locale.CANADA);
		int banknoteDenominations[] = {5,10,20,50};
		BigDecimal coinDenominations[] = {new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25), new BigDecimal(1.00), new BigDecimal(2.00)}; 
		int maxScaleWeight = 10000;
		int scaleSensitivity = 1;
		testStation = new SelfCheckoutStation(testCurrency, banknoteDenominations, coinDenominations, maxScaleWeight, scaleSensitivity);
		dummyPayment = new Payment(testStation);  
		dummyAddItem = new AddItem(testStation);
		
		// Inventory Stuff Below
		
		/// Apple, Barcode: 1111, Weight: 100g, Price: $1
		double appleWeight = 100;
		Numeral[] n = {Numeral.one, Numeral.one, Numeral.one, Numeral.one};
		b1 = new Barcode(n);
		BarcodedItem appleItem = new BarcodedItem(b1, 100.00);
		BarcodedProduct appleProduct = new BarcodedProduct(b1, "Apple", new BigDecimal(1.00), appleWeight);
		
		ArrayList<Object> pi = new ArrayList<>();
		pi.add(appleProduct);
		pi.add(appleItem);
		Inventory.addProductAndItemByBarcode(b1, pi);
		
		// Soda, Barcode: 2222, Weight: 50g, Price: $2
		double sodaWeight = 50;
		Numeral[] n2 = {Numeral.two, Numeral.two, Numeral.two, Numeral.two};
		b2 = new Barcode(n2);
		BarcodedItem sodaItem = new BarcodedItem(b2, 50.00);
		BarcodedProduct sodaProduct = new BarcodedProduct(b2, "Soda", new BigDecimal(2.00), sodaWeight);
		
		ArrayList<Object> pi2 = new ArrayList<>();
		pi2.add(sodaProduct);
		pi2.add(sodaItem);
		Inventory.addProductAndItemByBarcode(b2, pi2);
		
		// Chips, Barcode: 3333, Weight: 20g, Price: $3
		double chipWeight = 20;
		Numeral[] n3 = {Numeral.three, Numeral.three, Numeral.three, Numeral.three};
		b3 = new Barcode(n3);
		BarcodedItem chipsItem = new BarcodedItem(b3, 20.00);
		BarcodedProduct chipsProduct = new BarcodedProduct(b3, "Chips", new BigDecimal(3.00), chipWeight);
		
		ArrayList<Object> pi3 = new ArrayList<>();
		pi3.add(chipsProduct);
		pi3.add(chipsItem);
		Inventory.addProductAndItemByBarcode(b3, pi3);
		
		// Chocolate Bar, Barcode: 4444, Weight: 30g, Price: $4
		double chocolateWeight = 30;
		Numeral[] n4 = {Numeral.four, Numeral.four, Numeral.four, Numeral.four};
		b4 = new Barcode(n4);
		BarcodedItem chocolateItem = new BarcodedItem(b4, 30.00);
		BarcodedProduct chocolateProduct = new BarcodedProduct(b4, "Chocolate Bar", new BigDecimal(4.00), chocolateWeight);
		
		ArrayList<Object> pi4 = new ArrayList<>();
		pi4.add(chocolateProduct);
		pi4.add(chocolateItem);
		Inventory.addProductAndItemByBarcode(b4, pi4);
	}
	
	
	@Test
	public void testGetTotalPrice() {
		AddItem.resetTotal();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1); // $1 100g
		dummyAddItem.barcodeScanned(testStation.mainScanner, b4); // $4 30g
		assertTrue(5 == AddItem.getTotalPrice());
		
	}
	
	@Test
	public void testGetTotalWeight() {
		AddItem.resetTotalWeight();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1); // $1 100g
		dummyAddItem.weightChanged(testStation.baggingArea, 100);					//TODO : May need to change scanningArea to baggingArea
		dummyAddItem.barcodeScanned(testStation.mainScanner, b4); // $4 30g
		dummyAddItem.weightChanged(testStation.baggingArea, 30);					//TODO : May need to change scanningArea to baggingArea

		assertEquals(130, AddItem.getTotalWeight(), 0);
		
	}
	
	
	@Test
	public void testRemoveScannedItem_ItemScanned() {
		AddItem.resetScannedItems();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b2);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b3);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		Assert.assertTrue(AddItem.getScannedItems().contains(b3));
		dummyAddItem.removeScannedItem(b3);
		Assert.assertFalse(AddItem.getScannedItems().contains(b3));
		
	}
	
	@Test
	public void testRemoveScannedItem_ItemNotScanned() {
		AddItem.resetScannedItems();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b2);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b3);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b2);
		dummyAddItem.removeScannedItem(b1);
		
	}
	
	@Test
	public void testWeightChanged_expectedLessThanActualChange() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.weightChanged(testStation.scanningArea,  150.00);		//TODO : May need to change scanningArea to baggingArea
		testDisableAllButScale();
	}
	
	@Test
	public void testWeightChanged_expectedMoreThanActualChange() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.weightChanged(testStation.scanningArea,  50.00);		//TODO : May need to change scanningArea to baggingArea
		testDisableAllButScale();
	}
	
	@Test
	public void testWeightChanged_expectedEqualsActualChange() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.weightChanged(testStation.scanningArea,  100.00);		//TODO : May need to change scanningArea to baggingArea
		assertEquals(100.00, AddItem.getTotalWeight(), 0.001);
	}
	
	@Test
	public void testOverload() {
		dummyAddItem.overload(testStation.scanningArea);					//TODO : May need to change scanningArea to baggingArea
		testDisableAllButScale();
	}

	@Test
	public void testOutOfOverload() {
		dummyAddItem.outOfOverload(testStation.scanningArea);				//TODO : May need to change scanningArea to baggingArea
		assertFalse(testStation.mainScanner.isDisabled());
	}
	
	@Test
	public void testDisableAllButScale() {
		dummyAddItem.disableAllButScale();
		assertTrue(testStation.mainScanner.isDisabled());
		assertTrue(testStation.banknoteInput.isDisabled());
		assertTrue(testStation.coinSlot.isDisabled());
		assertTrue(testStation.banknoteValidator.isDisabled());
		assertTrue(testStation.coinValidator.isDisabled());
		assertFalse(testStation.scanningArea.isDisabled());				//TODO : May need to change scanningArea to baggingArea
	}
	
	@Test
	public void testBarcodeScannedPrice() 
	{
		// scanning Apple ($1)
		AddItem.resetTotal();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		assertEquals(1.00, AddItem.getTotalPrice(), 0);
	}
	
	@Test
	public void testAddProductAndItemByBarcode() {
		
		/// TestItem, Barcode: 7777, Weight: 10.00g, Price: $1.00
		double testWeight = 10;
		Numeral[] n = {Numeral.seven, Numeral.seven, Numeral.seven, Numeral.seven};
		Barcode b = new Barcode(n);
		BarcodedItem testItem = new BarcodedItem(b, 10.00);
		BarcodedProduct testProduct = new BarcodedProduct(b, "Test", new BigDecimal(1.00), testWeight);
		
		ArrayList<Object> pi = new ArrayList<>();
		pi.add(testProduct);
		pi.add(testItem);
		Inventory.addProductAndItemByBarcode(b, pi);
		
		ArrayList<Object> test = Inventory.getProductAndItemFromBarcode(b);
		assertTrue(test.get(0) == testProduct);
		
	}
	
	@Test
	public void testRemoveProductAndItemByBarcode() {
		
		/// TestItem, Barcode: 7777, Weight: 10.00g, Price: $1.00
		double testWeight = 10;
		Numeral[] n = {Numeral.seven, Numeral.seven, Numeral.seven, Numeral.seven};
		Barcode b = new Barcode(n);
		BarcodedItem testItem = new BarcodedItem(b, 10.00);
		BarcodedProduct testProduct = new BarcodedProduct(b, "Test", new BigDecimal(1.00), testWeight);
		
		ArrayList<Object> pi = new ArrayList<>();
		pi.add(testProduct);
		pi.add(testItem);
		Inventory.addProductAndItemByBarcode(b, pi);
		Inventory.removeProductAndItemByBarcode(b);
		
		assertTrue(null == Inventory.getProductAndItemFromBarcode(b));
	}
	
	@Test
	public void testGetProductAndItemFromBarcode() {
		double testWeight = 10;
		Numeral[] n = {Numeral.seven, Numeral.seven, Numeral.seven, Numeral.seven};
		Barcode b = new Barcode(n);
		BarcodedItem testItem = new BarcodedItem(b, 10.00);
		BarcodedProduct testProduct = new BarcodedProduct(b, "Test", new BigDecimal(1.00), testWeight);
		
		ArrayList<Object> piTest = new ArrayList<>();
		piTest.add(testProduct);
		piTest.add(testItem);
		Inventory.addProductAndItemByBarcode(b, piTest);
		
		ArrayList<Object> pi = Inventory.getProductAndItemFromBarcode(b);
		assertTrue(testProduct == pi.get(0));
		assertTrue(testItem == pi.get(1));
	}
	

	@Test
	public void testPaymentConstructorPass() {
		// Arrange, Act, Assert
		Assert.assertTrue(testStation.coinValidator.isDisabled());
		Assert.assertTrue(testStation.banknoteValidator.isDisabled());
	}

	@Test
	public void testWouldLikeToCheckOutAmountDuePass() {
		AddItem.resetScannedItemsCatalog();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b2);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b3);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b4);
		dummyPayment.wouldLikeToCheckOut(AddItem.getTotalPrice());
		Assert.assertEquals(dummyPayment.getAmountDue(), AddItem.getTotalPrice(), 0);
	}

	
	@Test
	public void testWouldLikeToCheckOutPass() {
		dummyPayment.wouldLikeToCheckOut(AddItem.getTotalPrice());
		
		Assert.assertTrue(testStation.baggingArea.isDisabled());						//TODO : May need to change scanningArea to baggingArea
		Assert.assertTrue(testStation.mainScanner.isDisabled());
		Assert.assertFalse(testStation.coinValidator.isDisabled());
		Assert.assertFalse(testStation.banknoteValidator.isDisabled());
	}
	
	@Test
	public void testValidCoinDetectedValGTamountDue() 
	{
		dummyPayment.wouldLikeToCheckOut(0.10);
		dummyPayment.validCoinDetected(testStation.coinValidator, new BigDecimal(0.25));
		testCheckoutFinished();
	}
	
	@Test
	public void testValidCoinDetectedvalLTamountDue() 
	{
		dummyPayment.wouldLikeToCheckOut(100.00);
		dummyPayment.validCoinDetected(testStation.coinValidator, new BigDecimal(0.25));
		Assert.assertTrue(0.25 == dummyPayment.getAmountPaid());
	}

	@Test
	public void testValidBanknoteDetectedValGTamountDue() 
	{
		dummyPayment.wouldLikeToCheckOut(10.00);
		dummyPayment.validBanknoteDetected(testStation.banknoteValidator, testCurrency, 50);
		testCheckoutFinished();
		
	}
	
	@Test
	public void testValidBanknoteDetectedValLTamountDue() 
	{
		dummyPayment.wouldLikeToCheckOut(100.00);
		dummyPayment.validBanknoteDetected(testStation.banknoteValidator, testCurrency, 5);
		Assert.assertTrue(5 == dummyPayment.getAmountPaid());
	}
	
	@Test
	public void testCheckoutFinished() 
	{
		AddItem.resetScannedItemsCatalog();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b3);
		dummyPayment.checkoutFinished();
		assertEquals(0, AddItem.getTotalPrice(), 0);
		assertEquals(0, dummyPayment.getAmountPaid(), 0);
		assertEquals(0, AddItem.getTotalWeight(), 0);
		assertTrue(AddItem.getScannedItemsCatalog().isEmpty());
		assertFalse(testStation.scanningArea.isDisabled());				//TODO : May need to change scanningArea to baggingArea
		assertFalse(testStation.mainScanner.isDisabled());
		assertTrue(testStation.coinValidator.isDisabled());
		assertTrue(testStation.banknoteValidator.isDisabled());
	}
	
	@Test
	public void testGetScannedItemsCatalog() {
		AddItem.resetScannedItemsCatalog();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b2);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b3);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b4);
		Assert.assertTrue(AddItem.getScannedItemsCatalog().equals("Apple     1\nSoda     2\nChips     3\nChocolate Bar     4\n"));
	}
	
	@Test
	public void testGetScannedItems() {
		AddItem.resetScannedItems();
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b4);
		dummyAddItem.barcodeScanned(testStation.mainScanner, b3);
		ArrayList<Barcode> testItems = new ArrayList<>();
		testItems.add(b1);
		testItems.add(b4);
		testItems.add(b3);
		
		Assert.assertTrue(testItems.equals(AddItem.getScannedItems()));
		
	}
	
	@Test
	public void testGetamountPaid() 
	{
		dummyPayment.wouldLikeToCheckOut(100.00);
		dummyPayment.validBanknoteDetected(testStation.banknoteValidator, testCurrency, 5);
		assertTrue(5 == dummyPayment.getAmountPaid());
	}
	
	@Test
	public void testGetamountDue() 
	{
		dummyPayment.wouldLikeToCheckOut(100.00);
		assertTrue(100.00 == dummyPayment.getAmountDue());
	}
	
	@Test
	public void testreturnToScanning() 
	{
		//arrange
		// act
		dummyPayment.returnToScanning();
		//assert
		Assert.assertFalse(testStation.scanningArea.isDisabled());					//TODO : May need to change scanningArea to baggingArea
		Assert.assertFalse(testStation.mainScanner.isDisabled());
		Assert.assertTrue(testStation.coinValidator.isDisabled());
		Assert.assertTrue(testStation.banknoteValidator.isDisabled());
	}
	
	@Test
	public void testResetScannedItemsCatalog() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		AddItem.resetScannedItemsCatalog();
		Assert.assertTrue(AddItem.getScannedItemsCatalog().isEmpty());
	}
	
	@Test
	public void testResetTotal() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		AddItem.resetTotal();
		Assert.assertTrue(0 == AddItem.getTotalPrice());
	}
	
	@Test
	public void testResetScannedItems() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		AddItem.resetScannedItems();
		Assert.assertTrue(AddItem.getScannedItems().isEmpty());
	}
	
	@Test
	public void testResetTotalWeight() {
		dummyAddItem.barcodeScanned(testStation.mainScanner, b1);
		AddItem.resetTotalWeight();
		Assert.assertTrue(0 == AddItem.getTotalWeight());
		
		
	}
	
	@Test
	public void testScanMembershipCard() throws IOException {
		card = new Card("MEMBERSHIP", "00000", "Holder", "000", "0000", true, true);
		dummyPayment.checkMembership(card);
		Assert.assertTrue("00000" == dummyPayment.getMembership());
	}
	
	@Test
	public void testReceiveChange() {
		
	}
	
	// tests paying with tap
	@Test
	public void testTapCredit() {
		
	}
	
	@Test
	public void testTapDebit() {
		
	}
	
	// tests paying with swipe
	@Test
	public void testSwipeCredit() {
		
	}
	
	@Test
	public void testSwipeDebit() {
		
	}
	
	// tests paying with insert
	@Test
	public void testInsertCredit() {
		
	}
	
	@Test
	public void testInsertDebit() {
		
	}
	
	@Test
	public void testFailToPlaceItem() {
		
	}
	
	@Test
	public void testAddItemAfterPartialPayment() {
		
	}
	// Testcase for instantiating inventory class (used for coverage)
	@Test
	public void inventoryInstantion() {
		Inventory inv = new Inventory();
	}
}
