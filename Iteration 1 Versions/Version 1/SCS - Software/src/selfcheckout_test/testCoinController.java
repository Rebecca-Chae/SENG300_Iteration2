package selfcheckout_test;
import selfcheckout_software.*;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

import junit.extensions.TestSetup;

public class testCoinController {
	
	private BigDecimal coinValue;
	private CoinController test_object;
	private Coin c;
	private CoinValidator v;
	private Currency currency = Currency.getInstance("CAD");
	private List<BigDecimal> denoms;
	
	@Before 
	public void TestSetup() {
		this.coinValue = new BigDecimal(1);
		//this.c = new Coin(currency, coinValue);	
		this.denoms = Arrays.asList(new BigDecimal(1));
		this.test_object = new CoinController(c);
		this.v = new CoinValidator(currency, denoms);
	}
	
	@Test
	public void testValidCoinDetected() {
		BigDecimal value = new BigDecimal(5);
		test_object.validCoinDetected(v, value);
		assertEquals(test_object.getAvailableFunds(), value);
	}
	
	@Test
	public void testStorageUnitFull() {
		test_object.setStorageFull(true);
		
		BigDecimal value = new BigDecimal(5);
		BigDecimal expectedValue = new BigDecimal(0);
		test_object.validCoinDetected(v, value);
		assertEquals(test_object.getAvailableFunds(), expectedValue);
	}
	
	@Test
	public void testCoinsUnloaded() {
		CoinStorageUnit unit = new CoinStorageUnit(100);
		test_object.coinsUnloaded(unit);
		assertEquals(test_object.getStorageFull(), false);
	}
	
	@Test
	public void testCoinsFull() {
		CoinStorageUnit unit = new CoinStorageUnit(100);
		test_object.coinsFull(unit);
		assertEquals(test_object.getStorageFull(), true);
	}
	
	
	
	
	
	
	

}
