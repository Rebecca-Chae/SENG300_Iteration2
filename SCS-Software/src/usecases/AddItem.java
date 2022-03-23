package usecases;


import java.math.BigDecimal;
import java.util.ArrayList;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;

public class AddItem implements ElectronicScaleObserver, BarcodeScannerObserver {
	
	private SelfCheckoutStation checkoutStation;
	private static StringBuilder scannedItemsCatalog;
	private static double totalPrice;
	private static double totalWeight;
	private double expectedWeight;
	private static ArrayList<Barcode> scannedItems;
	
	public AddItem(SelfCheckoutStation scs){
		checkoutStation = scs;
		checkoutStation.scanner.attach(this);
		checkoutStation.scale.attach(this);
		scannedItemsCatalog = new StringBuilder();
		scannedItems = new ArrayList<>();
	}
	
	public static double getTotalWeight() {
		return totalWeight;
	}
	
	public static double getTotalPrice() {
		return totalPrice;
	}

	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

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
	
	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		if(weightInGrams > expectedWeight) {
			disableAllButScale();
			System.out.println("Unexpected Item placed in bagging area, Please remove last item placed on scale.");
		}
		else if(weightInGrams < expectedWeight) {
			disableAllButScale();
			System.out.println("Unexpected Item placed in bagging area, Please remove last item placed on scale.");
		}
		else {
			totalWeight += expectedWeight;
		}
		
	}

	@Override
	public void overload(ElectronicScale scale) {
		disableAllButScale();
		System.out.println("Too many Items on scale, please reduce weight by removing heavy items from the bagging area.");
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		checkoutStation.scanner.enable();
	}
	
	public void disableAllButScale(){
		checkoutStation.scale.enable();
		checkoutStation.scanner.disable();
		checkoutStation.banknoteInput.disable();
		checkoutStation.coinSlot.disable();
		checkoutStation.banknoteValidator.disable();
		checkoutStation.coinValidator.disable();
	}
	
	public static String getScannedItemsCatalog() {
		return scannedItemsCatalog.toString();
	}
	
	public static void resetScannedItemsCatalog() {
		scannedItemsCatalog.setLength(0);
	}
	
	public static void resetTotal() {
		totalPrice = 0;
	}
	
	public static ArrayList<Barcode> getScannedItems() {
		return scannedItems;
	}
	
	public static void resetScannedItems() {
		scannedItems.clear();
	}
	
	public static void resetTotalWeight() {
		totalWeight = 0;
	}
	
}