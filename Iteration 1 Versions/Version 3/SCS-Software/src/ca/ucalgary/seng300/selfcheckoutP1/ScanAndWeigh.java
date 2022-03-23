/*
 * SENG300 Group33
 * Project iteration 1
 * 
 * Group member:
 * Joshua Cordeiro-Zebkowitz
 * Ethan Macson
 * Spencer Mutch
 * Chenghou Si
 * Rei Tsunemi
 * Nhat Truong
 */

package ca.ucalgary.seng300.selfcheckoutP1;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;
import org.lsmr.selfcheckout.products.BarcodedProduct;

public class ScanAndWeigh implements BarcodeScannerObserver, ElectronicScaleObserver {
	private Map<Barcode, BarcodedProduct> productDatabase;
	private Map<Barcode, BarcodedItem> itemDatabase;
	private double totalWeight;
	private double expectedWeight;
	private double previousWeight;
	private BigDecimal totalPrice;
	
	public ScanAndWeigh(Map<Barcode, BarcodedProduct> allProduct, Map<Barcode, BarcodedItem> allItem) {
		totalWeight = 0;
		previousWeight = 0;
		totalPrice = new BigDecimal(0);
		this.productDatabase = allProduct;
		this.itemDatabase = allItem;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}

	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		if ((weightInGrams - previousWeight) == expectedWeight) {
			previousWeight = totalWeight;
			totalWeight = weightInGrams;
		}
		else {
			throw new SimulationException("Weight added to scale does not match the expected weight.");
		}
		
	}

	@Override
	public void overload(ElectronicScale scale) {
		System.out.println("The scale is overloaded. Please remove items.");
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		System.out.println("The scale is no longer overloaded. You may resume scanning.");
	}

	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		BarcodedProduct product = productDatabase.get(barcode);
		BarcodedItem item = itemDatabase.get(barcode);
		
		double weight = item.getWeight();
		BigDecimal price = product.getPrice();
		
		totalPrice = totalPrice.add(price);
		expectedWeight = weight;
		
	}
	
	public double getTotalWeight() {
		return totalWeight;
	}
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

}