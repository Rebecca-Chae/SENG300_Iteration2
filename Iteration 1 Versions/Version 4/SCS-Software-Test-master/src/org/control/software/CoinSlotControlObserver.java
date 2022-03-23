package org.control.software;

import java.math.BigDecimal;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.CoinSlotObserver;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.DisabledException;

/**
 * Observes events emanating from a coin slot.
 *
 */

public class CoinSlotControlObserver implements CoinSlotObserver{
	private SelfCheckoutStation checkoutStation;
	int totalCoins;
	int totalAmount;
	BigDecimal coinValue;
	
	
	/**
	 * Enables the Abstract device.
	 * @param device
	 * 				The specified device to enable.
	 * 		
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.coinSlot.enable();
	
	}
	
	/**
	 * Disables the Abstract device.
	 * @param device
	 * 				The specified device to disable.
	 * 		
	 */

	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.coinSlot.disable();
		
	}
	
	/**
	 * An event announcing that a coin has been inserted.
	 * 
	 * @param slot
	 *             The device on which the event occurred.
	 */
	public void coinInserted(CoinSlot slot) {
		
		Coin coin = new Coin(coinValue);
		try {
			slot.accept(coin);
		} catch (DisabledException e) {
			e.printStackTrace();
		}
		
	}
}
