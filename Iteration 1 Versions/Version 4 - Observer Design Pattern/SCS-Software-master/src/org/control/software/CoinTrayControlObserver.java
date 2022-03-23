package org.control.software;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CoinTrayObserver;

/**
 * Observes events emanating from a coin tray. 
 * 
 */

public class CoinTrayControlObserver implements CoinTrayObserver {

	private SelfCheckoutStation checkoutStation;
	BigDecimal coinValue;
	
	/**
	 * Enables the Abstract device.
	 * @param device
	 * 				The specified device to enable.
	 * 		
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		checkoutStation.coinTray.enable();
	}

	/**
	 * Disables the Abstract device.
	 * @param device
	 * 				The specified device to disable.
	 * 		
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		checkoutStation.coinTray.disable();
		
	}

	/**
	 * Announces that a coin has been added to the indicated tray.
	 * 
	 * @param tray
	 *            The tray where the event occurred.
	 */
	public void coinAdded(CoinTray tray) {
		
		Coin coin = new Coin(coinValue);
		try {
			tray.accept(coin);
		} catch (DisabledException | OverloadException e) {
			e.printStackTrace();
		}	
		
	}

}
