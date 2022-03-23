package test.control.software;

import org.control.software.CheckoutController;
import org.control.software.ReceiptPrinterControlObserver;
import org.junit.Assert;
import org.junit.Test;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;

public class CheckoutTest {

	@Test
	public void test() {
		CheckoutController c = new CheckoutController();
		Assert.assertTrue(c.checkout());
	}
	
	
	// Tests if the observer is seeing the correct flag of out of paper
	@Test
	public void noPaper() {
		ReceiptPrinterControlObserver printerOb = new ReceiptPrinterControlObserver();
		ReceiptPrinter printer = new ReceiptPrinter();
			printerOb.outOfPaper(printer);
			Assert.assertTrue(printerOb.outOfPaper);
	
	}
	// Tests if the observer is seeing the correct flag of out of paper
	@Test
	public void noInk() {
		ReceiptPrinterControlObserver printerOb = new ReceiptPrinterControlObserver();
		ReceiptPrinter printer = new ReceiptPrinter();
			printerOb.outOfInk(printer);
			Assert.assertTrue(printerOb.outOfInk);
	
	}

}

