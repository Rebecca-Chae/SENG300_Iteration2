package testcases;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import usecases.AddItem;
import usecases.Inventory;
import usecases.Payment;

public class PaymentTest {
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
	public void test() {
		fail("Not yet implemented");
	}

}
