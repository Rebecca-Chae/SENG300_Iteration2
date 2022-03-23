package test.control.software;

import java.util.Currency;
import org.control.software.BanknoteValidatorControlObserver;
import org.junit.Assert;
import org.junit.Test;
import org.lsmr.selfcheckout.devices.BanknoteValidator;


public class BanknotePaymentTest {
	

	@Test
	public void validBanknoteDetecedTest() {
		
		BanknoteValidatorControlObserver validatorObserver = new BanknoteValidatorControlObserver();
		Currency cad = Currency.getInstance("CAD");
		int banknotes[] = {25, 50, 100};
		BanknoteValidator validator = new BanknoteValidator(cad, banknotes);
		validatorObserver.validBanknoteDetected(validator, cad, 100);
		Assert.assertEquals(validatorObserver.totalValue, 100);
		
	}
	
	
	// Test to toggle the observer on and off
	// TODO : currently not working or complete.
	@Test
	public void validBVCOToggle() {
		
		BanknoteValidatorControlObserver validatorObserver = new BanknoteValidatorControlObserver();
		//validatorObserver.enabled(null);
		//Assert.assertTrue();
		
	}

}
