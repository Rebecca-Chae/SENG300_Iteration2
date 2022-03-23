package org.control.software;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;


/**
 * Simulates a products database.
 *
 */
public class ProductsCollection {
	
	Map<Barcode, String> barcodesMap = new HashMap<Barcode, String>();
	Map<Barcode, String> descriptionsMap = new HashMap<>();
	Map<Barcode, BigDecimal> pricesMap = new HashMap<Barcode, BigDecimal>();
	Map<Barcode, ItemsCollection> itemsMap = new HashMap<Barcode, ItemsCollection>();
	
	public Barcode milkBarcode;
	public Barcode riceBarcode;
	
	/**
	 * Constructor that initializes products collection.
	 */
	public ProductsCollection() {
		this.initializeProducts();
	}
	
	
	/**
	 * Initializes products with barcodes, prices, and descriptions.
	 */
	public void initializeProducts() {
		
		Numeral[] n = {Numeral.one, Numeral.two, Numeral.three, Numeral.four};
		
		milkBarcode = new Barcode(n);
		riceBarcode = new Barcode(n);

		barcodesMap.put(milkBarcode, "Carton of Milk");
		barcodesMap.put(riceBarcode, "Bag of rice");
		
		pricesMap.put(milkBarcode, new BigDecimal("2.99"));
		pricesMap.put(riceBarcode, new BigDecimal("4.99"));
		
		descriptionsMap.put(milkBarcode, "Drink");
		descriptionsMap.put(riceBarcode, "Grains");
		
		itemsMap.put(milkBarcode, new ItemsCollection(milkBarcode, 1.00));
		itemsMap.put(riceBarcode, new ItemsCollection(riceBarcode, 0.5));
		
	}
	
	
	/**
	 * Gets a set of products and their barcodes.
	 * 
	 * @return barcodesMap set
	 */
	public Set<Entry<Barcode, String>> getProducts() {
		
		return barcodesMap.entrySet();
		
	}
	
	
	/**
	 * Gets price for a specified product.
	 * 
	 * @param barcode
	 * 				Product barcode.
	 * 
	 * @return price
	 * 				Price of product.
	 */
	public BigDecimal getPrice(Barcode barcode) {
		
		BigDecimal price = new BigDecimal("0.0");
		
		if(pricesMap.containsKey(barcode)) {
			price = pricesMap.get(barcode);
		}
		
		return price;
		
	}
	
	/**
	 * Gets description for a specified product.
	 * 
	 * @param barcode
	 * 				Product barcode.
	 * 
	 * @return description
	 * 				Description of product.
	 */
	public String getDescription(Barcode barcode) {
		
		String description = "";
		
		if(descriptionsMap.containsKey(barcode)) {
			description = descriptionsMap.get(barcode);
		}
		
		return description;
		
	}
	
	/**
	 * Gets all barcodes.
	 * 
	 * @return barcodesMap keys
	 * 				All barcodes.
	 */
	public Set<Barcode> getBarcodes() {
		return barcodesMap.keySet();
	}

}
