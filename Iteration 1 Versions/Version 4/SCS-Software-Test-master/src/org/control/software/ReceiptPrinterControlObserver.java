package org.control.software;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ReceiptPrinterObserver;

/**
 * Observes events emanating from a receipt printer.
 */
public class ReceiptPrinterControlObserver implements ReceiptPrinterObserver {
	
	private SelfCheckoutStation checkoutStation;
	public boolean outOfPaper;
	public boolean outOfInk;
	
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.printer.enable();
		
	}

	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.printer.disable();
		
	}
	
	/**
	 * Announces that the indicated printer is out of paper.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	public void outOfPaper(ReceiptPrinter printer) {
		outOfPaper = true;
	}

	/**
	 * Announces that the indicated printer is out of ink.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	public void outOfInk(ReceiptPrinter printer) {
		outOfInk = true;
	}

	/**
	 * Announces that paper has been added to the indicated printer.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	public void paperAdded(ReceiptPrinter printer) {
		checkoutStation.printer.addInk(checkoutStation.printer.MAXIMUM_PAPER);
	}

	/**
	 * Announces that ink has been added to the indicated printer.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	public void inkAdded(ReceiptPrinter printer) {
		checkoutStation.printer.addInk(checkoutStation.printer.MAXIMUM_INK);
	}
}

