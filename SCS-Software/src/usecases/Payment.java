package usecases;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.Card.CardInsertData;
import org.lsmr.selfcheckout.Card.CardSwipeData;
import org.lsmr.selfcheckout.Card.CardTapData;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

public class Payment implements CoinValidatorObserver, BanknoteValidatorObserver {
	private SelfCheckoutStation station;
	private double amountDue;
	private double amountPaid;
	private boolean isCheckingOut; //Not currently used other than being updated but could be useful for future iterations.
	private boolean hasMembership;
	private String membershipNumber;
	
	public Payment(SelfCheckoutStation checkout) {
		station = checkout;
		station.coinValidator.attach(this);
		station.banknoteValidator.attach(this);
		
		isCheckingOut = false;
		
		station.cardReader.disable();
		station.banknoteValidator.disable();
		station.coinValidator.disable();
		station.printer.addPaper(500);
		station.printer.addInk(500);
	}

	public void wouldLikeToCheckOut(double totalPrice) {
		amountDue = totalPrice;
		amountPaid = 0;
		isCheckingOut = true;
		station.baggingArea.disable();											//TODO : Need to change scale to baggingArea and / or scanningArea
		station.mainScanner.disable();											//TODO : Need to change scanner to mainScanner and / or handheldScanner
		station.banknoteValidator.enable();
		station.coinValidator.enable();
		station.cardReader.enable();
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
	
	
	public void checkMembership (Card card) throws IOException {
		CardData cardData = station.cardReader.swipe(card);
		if (cardData.getType() == "Membership") {
			hasMembership = true;
			membershipNumber = cardData.getNumber();
		}
		
	}
	public void returnToScanning() {
		//any amount inserted would be returned or stored. but this is not implemented yet as not required in this iteration.
		isCheckingOut = false;
		station.baggingArea.enable();										//TODO : Need to change scale to baggingArea and / or scanningArea
		station.mainScanner.enable();										//TODO : Need to change scanner to mainScanner and / or handheldScanner
		station.coinValidator.disable();
		station.banknoteValidator.disable();		
	}
	
	public void checkoutFinished() {
		if (hasMembership == true) {
			char [] memberNumber = membershipNumber.toCharArray();
			for (char ch: memberNumber) {
				station.printer.print(ch);
			}
		}
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
		station.baggingArea.enable();										//TODO : Need to change scale to baggingArea and / or scanningArea
		station.mainScanner.enable();											//TODO : Need to change scanner to mainScanner and / or handheldScanner
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
