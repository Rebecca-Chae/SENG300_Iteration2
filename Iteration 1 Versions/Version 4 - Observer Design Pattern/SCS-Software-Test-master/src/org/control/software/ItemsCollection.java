package org.control.software;

import java.util.HashMap;
import java.util.Map;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.SimulationException;


public class ItemsCollection extends Item {
	
	public double weightInGrams;
	
	Map<Barcode, Double> weightsMap = new HashMap<Barcode, Double>();

	/**
	 * Constructs an item with the indicated weight.
	 * 
	 * @param weightInGrams
	 *            The weight of the item.
	 * @throws SimulationException
	 *             If the weight is &le;0.
	 */
	public ItemsCollection(Barcode barcode, Double weightInGrams) throws NullPointerException {
		super(weightInGrams);
		if(weightInGrams <= 0.0)
			throw new SimulationException(new IllegalArgumentException("The weight has to be positive."));

		this.weightInGrams = weightInGrams;
		weightsMap.put(barcode, weightInGrams);
	}

	/**
	 * The weight of the item, in grams.
	 * 
	 * @return The weight in grams.
	 */
	public double getWeight() {
		return weightInGrams;
	}

}
