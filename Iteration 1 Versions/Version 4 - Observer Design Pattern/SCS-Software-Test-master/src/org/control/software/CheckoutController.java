package org.control.software;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

public class CheckoutController {
	
	BanknoteValidatorControlObserver banknoteValidator = new BanknoteValidatorControlObserver();
	CoinValidatorControlObserver coinValidator = new CoinValidatorControlObserver();
	ReceiptPrinterControlObserver receiptPrinter = new ReceiptPrinterControlObserver();
	public SelfCheckoutStation checkoutStation;
	public boolean checkedOut;
	
	public boolean checkout() {
		
		int checkoutTotal = banknoteValidator.totalValue + coinValidator.totalValue;
		checkoutStation.printer.print('C');
		return checkedOut = true;
		
	}
	

}
