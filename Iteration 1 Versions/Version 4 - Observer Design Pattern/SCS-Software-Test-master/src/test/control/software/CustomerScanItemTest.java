package test.control.software;

import org.control.software.ItemsCollection;
import org.control.software.ProductsCollection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.SimulationException;

public class CustomerScanItemTest {
	Barcode testBarcode;
	
	// Generates dummy barcode;
	@Before
	public void barcodeSetup() {
		Numeral[] n = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};		
		Barcode testBarcode = new Barcode(n);
	}
	
	// TODO : Products collection properly instantiates items.
	// Not complete
	@Test
	public void test2() {
		ProductsCollection pCollection = new ProductsCollection();
		pCollection.initializeProducts();
		//Assert.assertEquals(pCollection.getDescription(testBarcode);
	}
	
	// Tests if item collection properly returns null, when instantiating a null item
	@Test
	public void nullItem() {
		ItemsCollection iCollection = new ItemsCollection(null, 1.0);
		iCollection.weightInGrams = 0.0;
		Assert.assertNull(iCollection);
		//Assert.assertNull(iCollection);
	}
	
	// Test to determine if item collection returns exception for negative weight
	// TODO: Completely commented out as I could not get it to work properly
	/*
	@Test 
	void negWeightItem() {

		//ItemsCollection iCollection = new ItemsCollection(testBarcode, -1.0);
		Assert.assertThrows(IllegalArgumentException.class, new ItemsCollection(testBarcode, -1.0));

	}*/
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	// Test to determine if item collection returns exception for negative weight
	// TODO : attempt #2 - ECL Lemma not returning as covered
	@Test
	public void negWeightItem2() throws SimulationException {
		exception.expect(SimulationException.class);
		ItemsCollection iCollection = new ItemsCollection(testBarcode, -1.0);
	}
	
	@Test
	public void posWeightItem() {
		double dummyWeight;
		ItemsCollection iCollection = new ItemsCollection(testBarcode, 5.2);
		dummyWeight = iCollection.getWeight();
		Assert.assertEquals(5.2, dummyWeight, 0);
	}
}
