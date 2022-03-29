package usecases;

import java.util.ArrayList;
import java.util.HashMap;

import org.lsmr.selfcheckout.*;

/**
 * Represents the inventory of products and items.
 */
// no logic behind testing constructor to achieve 100% test coverage
public class Inventory {
	
	// ArrayList<Object>
	// [0] = Product
	// [1] = Item
	
	private static HashMap<Barcode, ArrayList<Object>> productInventory = new HashMap<>();
	
	/**
	 * Simulates the action of adding a product and item by barcode.
	 * @param  b 
	 * 			The barcode of the product and item.
	 * @param  pi
	 * 			The product and item to be added.
	 */
	public static void addProductAndItemByBarcode(Barcode b, ArrayList<Object> pi) {
		productInventory.put(b,pi);
	}
	
	/**
	 * Simulates the action of removing a product and item by barcode.
	 * @param  b 
	 * 			The barcode of the product and item.
	 */
	public static void removeProductAndItemByBarcode(Barcode b) {
		productInventory.remove(b);
	}
	
	/**
	 * Gets the product and item by barcode.
	 * @param  b 
	 * 			The barcode of the product and item.
	 * @return The product and item with barcode 'b'.
	 */
	public static ArrayList<Object> getProductAndItemFromBarcode(Barcode b) {
		return productInventory.get(b);
	}
	
}
