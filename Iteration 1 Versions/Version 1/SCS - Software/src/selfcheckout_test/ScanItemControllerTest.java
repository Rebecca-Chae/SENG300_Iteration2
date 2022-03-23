package selfcheckout_test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import junit.extensions.TestSetup;

import org.lsmr.selfcheckout.*;

import selfcheckout_software.ScanItemController;
import selfcheckout_software.*;
public class ScanItemControllerTest {
	private BigDecimal total;
	private double expectedWeight;
	private ArrayList<BarcodedProduct> productList;
	private ArrayList<BarcodedItem> itemlist;
	private ScanItemController scanner;
	@Before
	public void Setup() {
		productList = new ArrayList<>();
		itemlist = new ArrayList<>();
		scanner = new ScanItemController();
	}
	
	/*
	 * Test case for adding a product which is null.
	 * A SimulationException should be thrown in this case.
	 */	
	@Test(expected = SimulationException.class)
	public void nullProduct() {
		Numeral[] test_number= {Numeral.one,Numeral.one,Numeral.one,Numeral.one};
		Barcode barcode = new Barcode(test_number);
		
		Numeral[] test_random= {Numeral.two,Numeral.one,Numeral.one,Numeral.one};
		Barcode barcode_2 = new Barcode(test_random);
		
		BigDecimal price = new BigDecimal("10.00");
		BarcodedProduct barcodedProduct = new BarcodedProduct(barcode_2, "N/A", price);
		
		
		BarcodedProduct[] productList = {barcodedProduct};
		BarcodedItem[] itemList = {};

		scanner.addItem(barcode, productList, itemList);
	}

	/*
	 * Test case for adding an item which is null.
	 * A SimulationException should be thrown in this case.
	 */
	@Test(expected = SimulationException.class)
	public void nullItem() {
		Numeral[] test_number= {Numeral.one,Numeral.one,Numeral.one,Numeral.one};
		Barcode barcode = new Barcode(test_number);
		
		Numeral[] test_random= {Numeral.two,Numeral.one,Numeral.one,Numeral.one};
		Barcode barcode_2 = new Barcode(test_random);
		
		BigDecimal price = new BigDecimal("10.00");
		BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, "N/A", price);
		BarcodedProduct productList[] = {barcodedProduct};
		
		
		BarcodedItem item = new BarcodedItem(barcode_2, 10.00);
		BarcodedItem itemlist[] = {item};

		scanner.addItem(barcode, productList, itemlist);
	}

	/*
	 * Test case for ensuring price total is correct.
	 * total should equal 10.00 in this case.
	 */
	@Test
	public void correctTotal() {

		
		Numeral[] test_number= {Numeral.one,Numeral.one,Numeral.one,Numeral.one};
		//Create an item
		BigDecimal price = new BigDecimal("10.00");
		Barcode barcode = new Barcode(test_number);
		
		BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, "N/A", price);
		
		BarcodedProduct productList[] = {barcodedProduct};
		
		BarcodedItem item = new BarcodedItem(barcode, 10.00);
		BarcodedItem itemlist[] = {item};
		ScanItemController scanner = new ScanItemController();

		scanner.addItem(barcode, productList, itemlist);

		total = scanner.getTotal();
		
		assertEquals(price, total);
	}

	/*
	 * Test case for ensuring weight total is correct.
	 * expectedWeight should equal 10.00 in this case.
	 */
	@Test
	public void correctWeight() {

		Numeral[] test_number= {Numeral.one,Numeral.one,Numeral.one,Numeral.one};
		//Create an item
		BigDecimal price = new BigDecimal("10.00");
		Barcode barcode = new Barcode(test_number);
		
		BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, "N/A", price);
		BarcodedProduct[] productList = {barcodedProduct};
		
		
		BarcodedItem item = new BarcodedItem(barcode, 10.00);
		BarcodedItem[] itemlist = {item};

		ScanItemController scanner = new ScanItemController();

		scanner.addItem(barcode, productList, itemlist);

		expectedWeight = scanner.getExpectedWeight();

		assertEquals(10.00, expectedWeight,1);
	}
}