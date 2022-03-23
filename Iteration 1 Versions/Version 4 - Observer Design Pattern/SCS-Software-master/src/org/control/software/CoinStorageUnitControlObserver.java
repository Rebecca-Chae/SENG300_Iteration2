package org.control.software;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CoinStorageUnitObserver;

/**
 * Observes events emanating from a coin storage unit.
 *
 */
public class CoinStorageUnitControlObserver implements CoinStorageUnitObserver {

	private SelfCheckoutStation checkoutStation;
	boolean coinsFull;
	BigDecimal coinValue;
	
	/**
	 * Enables the Abstract device.
	 * @param device
	 * 				The specified device to enable.
	 * 		
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		checkoutStation.coinStorage.enable();
		
	}

	/**
	 * Disables the Abstract device.
	 * @param device
	 * 				The specified device to disable.
	 * 		
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		checkoutStation.coinStorage.disable();
		
	}

	/**
	 * Announces that the indicated coin storage unit is full of coins.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinsFull(CoinStorageUnit unit) {
		
		if (unit.getCoinCount() > unit.getCapacity()) {
			coinsFull = true;
		}
		
	}

	/**
	 * Announces that a coin has been added to the indicated storage unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinAdded(CoinStorageUnit unit) {
		
		Coin coin = new Coin(coinValue);
		try {
			unit.accept(coin);
		} catch (DisabledException | OverloadException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Announces that the indicated storage unit has been loaded with coins.
	 * Used to simulate direct, physical loading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinsLoaded(CoinStorageUnit unit) {
		
		Coin coin = new Coin(coinValue);
		try {
			unit.load(coin);
		} catch (OverloadException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Announces that the storage unit has been emptied of coins. Used to
	 * simulate direct, physical unloading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void coinsUnloaded(CoinStorageUnit unit) {
		
		unit.unload();
		
	}

}
