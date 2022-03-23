package selfcheckout_test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import selfcheckout_software.BanknoteController;

public class BanknoteTest {
	private BanknoteController pay;
	private SelfCheckoutStation s;
	
	@Before
	public void setup() {	    
	    Currency currency = Currency.getInstance("CAD");
	    int[] banknoteDenoms = new int[] {5,10,20,50};
	    BigDecimal[] coinDenominations = new BigDecimal[]{new BigDecimal("0.05"), new BigDecimal("0.10"), 
	    		new BigDecimal("0.25"), new BigDecimal("0.50"), new BigDecimal("1.00"), new BigDecimal("2.00")};
	    
	    
	    s = new SelfCheckoutStation(currency,banknoteDenoms,coinDenominations,Integer.MAX_VALUE,1);
	    pay = new BanknoteController(s);
	}
	
	@Test
	public void testBanknoteControllerRegular() throws DisabledException, OverloadException {
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),10));
		assertEquals(pay.getCurrentFunds(),15);
		assertEquals(pay.getValidBanknotes(),2);
	}
	
	// Customer paying for a $10 item
	@Test
	public void testBanknoteControllerEnough() throws DisabledException, OverloadException {
		
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),10));
		assertEquals(pay.hasSufficientFunds(new BigDecimal(10)),true);
	}
	
	// Customer paying for a $10 item
	@Test
	public void testBanknoteControllerMany() throws DisabledException, OverloadException {
		for(int i = 0;i<500;i++) {
			s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
		}
		assertEquals(pay.getCurrentFunds(),500*5);
	}
	
	// Customer does not have sufficient funds for $10
	@Test
	public void testBanknoteControllerNotEnough() throws DisabledException, OverloadException {
		
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),10));
		assertEquals(pay.hasSufficientFunds(new BigDecimal(20)),false);
	}
	
	@Test
	public void testBanknoteControllerFull() throws DisabledException, OverloadException {
		for(int i = 0;i<s.BANKNOTE_STORAGE_CAPACITY;i++) {
			s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
			while(!s.banknoteInput.hasSpace()) {
				s.banknoteInput.removeDanglingBanknote();
				s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
			}
		}
		
		s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),5));
		
		assertEquals(5*s.BANKNOTE_STORAGE_CAPACITY,pay.getCurrentFunds());
	}
	
	@Test
	public void testBanknoteControllerInvalidBanknote() throws DisabledException, OverloadException {
		s.banknoteInput.accept(new Banknote(Currency.getInstance("USD"),5));
		assertEquals(0,pay.getCurrentFunds());
		assertEquals(1,pay.getInvalidBanknotes());
	}
	
    @Test
    public void testPayWithOnlyInvalidDenominationBanknotes() throws DisabledException, OverloadException {   
        s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),7));
        assertEquals(pay.getInvalidBanknotes(), 1);
    }
    
    @Test (expected = DisabledException.class)
    public void testPayWithDisabledBanknoteSlot() throws DisabledException, OverloadException {
        s.banknoteInput.disable();
        s.banknoteInput.accept(new Banknote(Currency.getInstance("CAD"),10));
    }    
	
	
}
