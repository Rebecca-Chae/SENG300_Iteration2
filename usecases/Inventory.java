package usecases;

import java.util.ArrayList;
import java.util.HashMap;

import org.lsmr.selfcheckout.*;

// no logic behind testing constructor to achieve 100% test coverage
public class Inventory {
	
	// ArrayList<Object>
	// [0] = Product
	// [1] = Item
	
	private static HashMap<Barcode, ArrayList<Object>> productInventory = new HashMap<>();
	
	public static void addProductAndItemByBarcode(Barcode b, ArrayList<Object> pi) {
		productInventory.put(b,pi);
	}
	
	public static void removeProductAndItemByBarcode(Barcode b) {
		productInventory.remove(b);
	}
	
	public static ArrayList<Object> getProductAndItemFromBarcode(Barcode b) {
		return productInventory.get(b);
	}
	
}
