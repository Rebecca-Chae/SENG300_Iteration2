package org.control.software;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;

/**
 * Observes events emanating from a barcode scanner.
 */
public class BarcodeScannerControlObserver implements BarcodeScannerObserver {
	
	private SelfCheckoutStation checkoutStation;
	BigDecimal totalValue = new BigDecimal(0.0);
	int totalItems;

	/**
	 * Enables barcode devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.scanner.enable();
		
	}

	/**
	 * Disables banknote devices.
	 * 
	 * @param device
	 * 				AbstractDevice of any type
	 */
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		checkoutStation.scanner.disable();
		
	}

	/**
	 * An event announcing that the indicated barcode has been successfully scanned.
	 * 
	 * @param barcodeScanner
	 *            The device on which the event occurred.
	 * @param barcode
	 *            The barcode that was read by the scanner.
	 */
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		
		ProductsCollection products = new ProductsCollection();
		BigDecimal price = products.getPrice(barcode);
		Item item = products.itemsMap.get(barcode);
		barcodeScanner.scan(item);
		totalItems++;
		totalValue = totalValue.add(price);
		
	}

}
