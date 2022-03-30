package usecases;


import java.io.IOException;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;
import org.lsmr.selfcheckout.products.*;

/**
 * Represents the use cases involved with adding or removing an item.
 */
public class AddItem implements ElectronicScaleObserver, BarcodeScannerObserver {
	
	private SelfCheckoutStation checkoutStation;
	private static StringBuilder scannedItemsCatalog;
	private static double totalPrice;
	private static double totalWeight;
	private double expectedWeight;
	private static ArrayList<Barcode> scannedItems;
	
	/**
	 * Create an AddItem instance.
	 * 
	 * @param scs
	 *            The self checkout station to be used.
	 */
	public AddItem(SelfCheckoutStation scs){
		checkoutStation = scs;
		checkoutStation.mainScanner.attach(this);					//TODO : Need to change scanner to mainScanner and / or handheldScanner
		checkoutStation.scanningArea.attach(this);					//TODO : Need to change scale to baggingArea and / or scanningArea
		checkoutStation.baggingArea.attach(this);						// Not so sure if this is necessary but just added for now
		scannedItemsCatalog = new StringBuilder();
		scannedItems = new ArrayList<>();
	}
	
	/**
	 * Gets the total weight.
	 * 
	 * @return The total weight on the scale.
	 */
	public static double getTotalWeight() {
		return totalWeight;
	}
	
	/**
	 * Gets the total price.
	 * 
	 * @return The total price.
	 */
	public static double getTotalPrice() {
		return totalPrice;
	}
	
	/**
	 * Simulates the action of adding a bag.
	 * @param  bag
	 * 			The bag to be added.
	 */
	public void addOwnBag(Item bag) {
		// Customer adds their own bag to the bagging area
		if (bag == null) {
			throw new SimulationException("The bag is null.");
		}
		checkoutStation.baggingArea.add(bag);
	}
	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	
	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	/**
	 * Simulates the action of scanning a barcode.
	 * @param  barcodeScanner
	 * 			The scanner to scan the barcode.
	 * @param  barcode 
	 * 			The barcode to be scanned.
	 */
	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		ArrayList<Object> pi = Inventory.getProductAndItemFromBarcode(barcode);
		
		BarcodedProduct p = (BarcodedProduct) pi.get(0);
		BarcodedItem i = (BarcodedItem) pi.get(1);
		
		BigDecimal price = p.getPrice();
		double d = price.doubleValue();
		totalPrice += d;
		
		double w = i.getWeight();
		expectedWeight = w;
		
		scannedItems.add(barcode);
		
		scannedItemsCatalog.append(p.getDescription() + "     " + p.getPrice() + "\n");
		
	}
	
	/**
	 * Simulates the action of removing a scanned item.
	 * @param  aBarcode 
	 * 			The barcode of the scanned item.
	 */
	public void removeScannedItem(Barcode aBarcode) {
		if(scannedItems.contains(aBarcode) == false) {
			System.out.println("This item was never scanned, it cannot be removed.");
			return;
		}
		ArrayList<Object> pi = Inventory.getProductAndItemFromBarcode(aBarcode);
		
		BarcodedProduct p = (BarcodedProduct) pi.get(0);
		BarcodedItem i = (BarcodedItem) pi.get(1);
		
		totalPrice -= p.getPrice().doubleValue();
		totalWeight -= i.getWeight();
		
		int start = scannedItemsCatalog.indexOf((p.getDescription() + "     " + p.getPrice() + "\n"));
		int end  = start + (p.getDescription() + "     " + p.getPrice() + "\n").length();
		scannedItemsCatalog.delete(start,end);
		
		scannedItems.remove(aBarcode);
	}
	
	/**
	 * Checks if the weight on the indicated scale has changed. Also checks if the customer fails to place an item.
	 * 
	 * @param scale
	 *            The scale to be checked.
	 * @param weightInGrams
	 *            The new weight in grams.
	 */
	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		FiveSecTimer t = new FiveSecTimer();
		t.start();
		
		if(weightInGrams > expectedWeight) {
			disableAllButScale();
			System.out.println("Unexpected Item placed in bagging area, Please remove last item placed on scale.");
		}
		else if(weightInGrams < expectedWeight || t.secondsPassed >= 5) {
			disableAllButScale();
			System.out.println("Please place item in bagging area.");
		}
		else {
			totalWeight += expectedWeight;
		}
	}
	
	/**
	 * Simulates the action of adding additional items after partial payment.
	 * @param  barcodeScanner
	 * 			The scanner to scan the barcode.
	 * @param  barcode 
	 * 			The barcode to be scanned.
	 */
	public void afterPartialPayment(BarcodeScanner barcodeScanner, Barcode barcode) {		
		double tempTotal = totalPrice;
		barcodeScanned(barcodeScanner, barcode);
		
		totalPrice = totalPrice - tempTotal;
	}

	/**
	 * Announces that excessive weight has been placed on the indicated scale.
	 * 
	 * @param scale
	 *            The scale where the event occurred.
	 */
	@Override
	public void overload(ElectronicScale scale) {
		disableAllButScale();
		System.out.println("Too many Items on scale, please reduce weight by removing heavy items from the bagging area.");
	}

	/**
	 * Announces that the former excessive weight has been removed from the
	 * indicated scale, and it is again able to measure weight.
	 * 
	 * @param scale
	 *            The scale where the event occurred.
	 */
	@Override
	public void outOfOverload(ElectronicScale scale) {
		checkoutStation.mainScanner.enable();						//TODO : Need to change scanner to mainScanner and / or handheldScanner
	}
	
	/**
	 * Simulates the action of disabling all devices but the scale.
	 */
	public void disableAllButScale(){
		checkoutStation.scanningArea.enable();								//TODO : Need to change scale to baggingArea and / or scanningArea
		checkoutStation.mainScanner.disable();							//TODO : Need to change scanner to mainScanner and / or handheldScanner
		checkoutStation.banknoteInput.disable();
		checkoutStation.coinSlot.disable();
		checkoutStation.banknoteValidator.disable();
		checkoutStation.coinValidator.disable();
	}
	
	/**
	 * Gets the catalog of scanned items.
	 * 
	 * @return The catalog of scanned items.
	 */
	public static String getScannedItemsCatalog() {
		return scannedItemsCatalog.toString();
	}
	
	/**
	 * Simulates the action of reseting the scanned items catalog.
	 */
	public static void resetScannedItemsCatalog() {
		scannedItemsCatalog.setLength(0);
	}
	
	/**
	 * Simulates the action of resetting the total price.
	 */
	public static void resetTotal() {
		totalPrice = 0;
	}
	
	/**
	 * Gets the list of scanned items.
	 * 
	 * @return The list of scanned items.
	 */
	public static ArrayList<Barcode> getScannedItems() {
		return scannedItems;
	}
	
	/**
	 * Simulates the action of resetting the scanned items.
	 */
	public static void resetScannedItems() {
		scannedItems.clear();
	}
	
	/**
	 * Simulates the action of resetting the total weight.
	 */
	public static void resetTotalWeight() {
		totalWeight = 0;
	}
	
}

class FiveSecTimer extends Thread {
	public int secondsPassed;
	
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed++;
		}
	};
	
	public void start() {
		timer.scheduleAtFixedRate(task,0,1000);
	}
	
}
