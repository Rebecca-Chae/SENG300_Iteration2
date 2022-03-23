package org.control.software;

import java.util.Currency;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteStorageUnitObserver;

/**
 * Observes events emanating from a banknote storage unit.
 */
public class BanknoteStorageUnitControlObserver implements BanknoteStorageUnitObserver {

	private SelfCheckoutStation checkoutStation;
	boolean banknotesFull;
	int noteValue;
	Currency cad = Currency.getInstance("CAD");
	
	/**
	 * Enables banknote devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.banknoteStorage.enable();
	}

	
	/**
	 * Disables banknote devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.banknoteStorage.disable();
		
	}

	/**
	 * Announces that the indicated banknote storage unit is full of banknotes.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void banknotesFull(BanknoteStorageUnit unit) {
		
		if(unit.getBanknoteCount() > unit.getCapacity()) {
			banknotesFull = true;
		}
		
	}

	/**
	 * Announces that a banknote has been added to the indicated storage unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void banknoteAdded(BanknoteStorageUnit unit) {
		
		Banknote banknote = new Banknote(cad, noteValue);
		try {
			unit.accept(banknote);
		} catch (DisabledException | OverloadException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Announces that the indicated storage unit has been loaded with banknotes.
	 * Used to simulate direct, physical loading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		
		Banknote banknote = new Banknote(cad, noteValue);
		try {
			unit.load(banknote);
		} catch (OverloadException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Announces that the storage unit has been emptied of banknotes. Used to
	 * simulate direct, physical unloading of the unit.
	 * 
	 * @param unit
	 *            The storage unit where the event occurred.
	 */
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		
		unit.unload();
		
	}

}
