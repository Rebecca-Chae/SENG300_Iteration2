package usecases;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

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
	private String member;
	private String cardNumber;
	private String cardholder;
	private String cvv;
	
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
	
	// Return change after calculating it
	public BigDecimal getChange(BigDecimal cost, BigDecimal paid) {
		if (paid.intValue() > cost.intValue()) {
			throw new InternalError("Bad call to function getChange: cost is greater than payment");
		}
		
		BigDecimal changeAmt = cost.subtract(paid);
		List<BigDecimal> denoms = station.coinDenominations;
		Collections.sort(denoms, Collections.reverseOrder()); // sort the list of denominations in descending order
		
		// greedy method to return coins equal to (or as close as possible to) changeAmt
		for(BigDecimal denom : denoms) { // for each denomination, starting with the largest
			while(changeAmt.subtract(denom).intValue() >= 0) { // if subtracting that denom from changeAmt is still positive, keep doing it
				try {
					station.coinDispensers.get(denom).emit();
				} catch (OverloadException | EmptyException | DisabledException e) {
					e.printStackTrace();
				}
				// emit the coin to the tray
				changeAmt = changeAmt.subtract(denom); // subtract from total
			}
		}	
		return changeAmt; // changeAmt still may not be zero, if the denominations do not allow for the exact change to be returned.
	}
	
	public void checkMembership (Card card) throws IOException {
		CardData cardData = station.cardReader.swipe(card);
		if (cardData.getType() == "MEMBERSHIP") {
			hasMembership = true;
			membershipNumber = cardData.getNumber();
			member = "Membership #";
		}
	}
	
	public void cardWithSwipe(Card card, double amount) {
		CardData cardData;
		try {
			cardData = station.cardReader.swipe(card);
			
			if (cardData.getType() == "DEBIT" || cardData.getType() == "CREDIT") {
				cardholder = cardData.getCardholder();
				cardNumber = cardData.getNumber();
				cvv = cardData.getCVV();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (checkValidation(cardholder, cardNumber, cvv)) {
			amountPaid += amount;			
		}
	}

	public void cardWithTap(Card card, double amount) {
		CardData cardData;
		try {
			cardData = station.cardReader.tap(card);
			
			if (cardData.getType() == "DEBIT" || cardData.getType() == "CREDIT") {
				cardholder = cardData.getCardholder();
				cardNumber = cardData.getNumber();
				cvv = cardData.getCVV();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (checkValidation(cardholder, cardNumber, cvv)) {
			amountPaid += amount;			
		}
	}
	
	public void cardWithInsert(Card card, String holder, double amount) {
		CardData cardData;
		cardholder = holder;
		try {
			cardData = station.cardReader.insert(card, holder);
			
			if (cardData.getType() == "DEBIT" || cardData.getType() == "CREDIT") {
				cardNumber = cardData.getNumber();
				cvv = cardData.getCVV();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (checkValidation(cardholder, cardNumber, cvv)) {
			amountPaid += amount;			
		}
	}
	
	private boolean checkValidation(String holder, String number, String value) {
		// Check the card holder
		if (holder == null || holder == "") {
			return false;
		}
		// Check the card number
		if (number == null || number == "") {
			return false;
		}
		// Check the cvv (card verification value)
		if (value == null || value.length() < 3) {
			return false;
		}
		return true;
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
			char [] memberProof = member.toCharArray();
			for (char ch: memberProof) {
				station.printer.print(ch);
			}
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
