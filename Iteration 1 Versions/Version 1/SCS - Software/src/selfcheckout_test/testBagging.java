package selfcheckout_test;
import selfcheckout_software.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.Acceptor;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

import junit.framework.Assert;

class ItemClass extends Item {
	ItemClass() {
		super(10);
	}
	ItemClass(double weight) {
		super(weight);
	}
}
public class testBagging {

	private ElectronicScale e;
	private ArrayList<Item> list;
	private Item item1;
	private Item item2;
	private Item item3;
	private Item item4;
	private baggingItem test;
	
	@Before
	public void setUp() throws Exception {
		this.e = new ElectronicScale(50,1);
		this.test = new baggingItem(e);
		item1 = new ItemClass(5);
		item2 = new ItemClass(60);
		item3 = new ItemClass(7);
		
	}

	@Test
	public void testBaggingItem() throws OverloadException{
		e.endConfigurationPhase();
		test.bagItem(item3, e);
		Assert.assertEquals(e.getCurrentWeight(),7.0,0.1);
		
	}
	
	@Test (expected=OverloadException.class)
	public void testOverweightLimit() throws OverloadException{
		e.endConfigurationPhase();
		test.bagItem(item2, e);

		}
	
	@Test (expected = SimulationException.class)
	public void testWrongItem() throws OverloadException{
		e.endConfigurationPhase();
		e.add(item3);
		test.bagItem(item1, e);
	}
	

}
