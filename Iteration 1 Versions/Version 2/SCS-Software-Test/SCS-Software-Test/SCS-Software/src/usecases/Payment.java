package usecases;

import java.math.BigDecimal;
import java.util.Currency;



import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

public class Payment implements CoinValidatorObserver, BanknoteValidatorObserver {
	private SelfCheckoutStation station;
	private double amountDue;
	private double amountPaid;
	private boolean isCheckingOut;
	
	public Payment(SelfCheckoutStation checkout) {
		station = checkout;
		station.coinValidator.attach(this);
		station.banknoteValidator.attach(this);
		
		isCheckingOut = false;
		
		station.banknoteValidator.disable();
		station.coinValidator.disable();
		station.printer.addPaper(500);
		station.printer.addInk(500);
	}

	public void wouldLikeToCheckOut(double totalPrice) {
		amountDue = totalPrice;
		amountPaid = 0;
		isCheckingOut = true;
		station.scale.disable();
		station.scanner.disable();
		station.banknoteValidator.enable();
		station.coinValidator.enable();
		System.out.println("Please proceed to checking out.");
	}
	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	// Called whenever a new coin is inserted and accepted in the machine
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		amountPaid += value.doubleValue();
		if(amountPaid >= amountDue) {
			checkoutFinished();
		}
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {}

	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		amountPaid += (double) value;
		if(amountPaid >= amountDue) {
			checkoutFinished();
		}
	}
	
	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {}
	
	public void returnToScanning() {
		//any amount inserted would be returned or stored. but this is not implemented yet as not required in this iteration.
		isCheckingOut = false;
		station.scale.enable();
		station.scanner.enable();
		station.coinValidator.disable();
		station.banknoteValidator.disable();		
	}
	
	public void checkoutFinished() {
		char [] scannedItems = AddItem.getScannedItemsCatalog().toCharArray();
		for(char ch: scannedItems) {
			station.printer.print(ch);
		}
		AddItem.resetScannedItemsCatalog();
		AddItem.resetTotal();
		AddItem.resetScannedItems();
		AddItem.resetTotalWeight();
		amountDue = 0;
		amountPaid = 0;
		station.scale.enable();
		station.scanner.enable();
		station.coinValidator.disable();
		station.banknoteValidator.disable();
		isCheckingOut = false;
	}
	
	public double getAmountPaid() {
		return amountPaid;
	}
	
	public double getAmountDue() {
		return amountDue;
	}
	
}
