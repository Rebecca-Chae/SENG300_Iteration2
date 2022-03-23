package org.control.software;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.BanknoteSlotObserver;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import java.util.Currency;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;

/**
 * Observes events emanating from a banknote slot.
 */
public class BanknoteSlotControlObserver implements BanknoteSlotObserver {
	
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
		
		checkoutStation.banknoteInput.enable();
		checkoutStation.banknoteOutput.enable();
	
	}

	/**
	 * Disables banknote devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.banknoteInput.disable();
		checkoutStation.banknoteOutput.disable();
		
	}
	
	/**
	 * An event announcing that a banknote has been inserted.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	public void banknoteInserted(BanknoteSlot slot){
		
		Banknote banknote = new Banknote(cad, noteValue);
		try {
			slot.accept(banknote);
		} catch (DisabledException | OverloadException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * An event announcing that a banknote has been returned to the user, dangling
	 * from the slot.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	public void banknoteEjected(BanknoteSlot slot){
		
		Banknote bankNote = new Banknote(cad, noteValue);
		try {
			slot.emit(bankNote);
		} catch (DisabledException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * An event announcing that a dangling banknote has been removed by the user.
	 * 
	 * @param slot
	 *            The device on which the event occurred.
	 */
	public void banknoteRemoved(BanknoteSlot slot){
		
		slot.removeDanglingBanknote();
		totalValue -= noteValue;
		totalBanknotes--;
		
	}
}
