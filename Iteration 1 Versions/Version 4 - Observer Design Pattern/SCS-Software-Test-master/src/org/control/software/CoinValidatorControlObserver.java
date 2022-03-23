package org.control.software;

import java.math.BigDecimal;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.CoinValidatorObserver;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.DisabledException;

/**
 * Observes events emanating from a coin validator.
 */
public class CoinValidatorControlObserver implements CoinValidatorObserver{
	private SelfCheckoutStation checkoutStation;
	public int totalCoins;
	public int totalValue;
	BigDecimal coinValue;

	/**
	 * Enables the Abstract device.
	 * @param device
	 * 				The specified device to enable.
	 * 		
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.coinValidator.enable();
	
	}

	/**
	 * Disables the Abstract device.
	 * @param device
	 * 				The specified device to disable.
	 * 		
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.coinValidator.disable();
		
	}
	
	/**
	 * An event announcing that the indicated coin has been detected and determined
	 * to be valid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 * @param value
	 *            The value of the coin.
	 */
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		coinValue = value;
		Coin coin = new Coin(coinValue);
		try {
			validator.accept(coin);
			totalCoins++;
			totalValue += coinValue.intValue();
		} catch (DisabledException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * An event announcing that a coin has been detected and determined to be
	 * invalid.
	 * 
	 * @param validator
	 *            The device on which the event occurred.
	 */
	
	public void invalidCoinDetected(CoinValidator validator) {
		boolean isValid = false;
		Coin coin = new Coin(coinValue);
		try {
			validator.accept(coin);
		} catch (DisabledException e) {
			isValid = true;
			e.printStackTrace();
		}
		
	}
}