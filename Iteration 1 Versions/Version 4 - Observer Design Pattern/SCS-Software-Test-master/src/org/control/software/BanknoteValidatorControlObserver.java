package org.control.software;
import java.util.Currency;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.BanknoteValidatorObserver;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.DisabledException;

/**
 * Observes events emanating from a banknote validator.
 */
public class BanknoteValidatorControlObserver implements BanknoteValidatorObserver{
	
	private SelfCheckoutStation checkoutStation;
	public int totalBanknotes;
	public int totalValue;
	public int noteValue;
	Currency cad = Currency.getInstance("CAD");
	
	/**
	 * Enables banknote devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.banknoteValidator.enable();
	
	}

	/**
	 * Disables banknote devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.banknoteValidator.disable();
		
	}
	
	/**
	 * An event announcing that the indicated banknote has been detected and
	 * determined to be valid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 * @param currency
	 *            The kind of currency of the inserted banknote.
	 * @param value
	 *            The value of the inserted banknote.
	 */
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		Banknote banknote = new Banknote(currency, value);
		boolean isValid = false;
		try {
			validator.accept(banknote);
			isValid = true;
			totalBanknotes++;
			totalValue += value;
		} catch (DisabledException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * An event announcing that the indicated banknote has been detected and
	 * determined to be invalid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 */
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		
		Banknote banknote = new Banknote(cad, noteValue);
		boolean isValid = false;
		try {
			validator.accept(banknote);
			isValid = true;
		} catch (DisabledException e) {
			e.printStackTrace();
		}
		
	}
}