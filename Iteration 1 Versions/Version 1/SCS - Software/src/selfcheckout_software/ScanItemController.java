package selfcheckout_software;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;

public class ScanItemController implements BarcodeScannerObserver  {
	
	private BigDecimal total = new BigDecimal("0.00");
	private double expectedWeight;
	
	public ScanItemController() {
		
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Adds the weight and price of the item and product related to the barcode
	 * 
	 * @param barcode
	 *            The barcode that was scanned in
	 * @param productList
	 * 			  List of all products with a barcode
	 * @param itemList
	 * 			  List of all items with a barcode
	 * @throws SimulationException
	 *             If there is no product or item with the respective barcode
	 */
	public void addItem(Barcode barcode, BarcodedProduct[] productList, BarcodedItem[] itemList){
		BarcodedProduct product = null;
		BarcodedItem item = null;
		
		
		for(int i = 0; i < productList.length; i++) {
			if(barcode==productList[i].getBarcode()) {
				product = productList[i];
				break;
			}
		}
		
		if(product ==null ) throw new SimulationException("Barcode does not exist in products");
		
		for(int i = 0; i < itemList.length; i++) {
			if(barcode==itemList[i].getBarcode()) {
				item = itemList[i];
				break;
			}
		}
		if(item == null) throw new SimulationException("Barcode does not exist in items");
		
		total = total.add(product.getPrice());
		expectedWeight += item.getWeight();
	}
	
		public BigDecimal getTotal() {
		return total;
	}
	
	public double getExpectedWeight() {
		return expectedWeight;
	}	
}
