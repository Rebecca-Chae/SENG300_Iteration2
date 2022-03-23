package selfcheckout_test;
import selfcheckout_software.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;

import junit.extensions.TestSetup;
import junit.framework.Assert;

public class test_wish_to_checkout {

	private wish_checkout test_object;
	private AbstractDeviceObserverStub payment;
	private ElectronicScaleObserverStub w_observer;
	private ElectronicScale E;
	
	@Before
	public void TestSetup() {
		this.E = new ElectronicScale(100, 1);
		this.test_object = new wish_checkout(E);
		this.payment = new AbstractDeviceObserverStub();
		this.w_observer = new ElectronicScaleObserverStub();
		
	}
	
	@Test
	public void test_attachment() throws OverloadException, DisabledException {
		test_object.endConfigurationPhase();
		E.endConfigurationPhase();
		test_object.wish_to_checkout(payment);
		test_object.detach(payment);
		assertFalse(test_object.detach(payment));
	}
	
	@Test(expected = SimulationException.class)
	public void test_weight_changed() throws OverloadException, DisabledException {
		E.attach(w_observer);
		Item test_item1 = new Item(20) {};
		Item test_item2 = new Item(40) {};
		E.endConfigurationPhase();
		test_object.endConfigurationPhase();
		E.add(test_item1);
		E.enable();
		E.add(test_item2);

		test_object.wish_to_checkout(payment);
		
	}
	
	@Test(expected = SimulationException.class)
	public void test_fail_to_accept()throws OverloadException,DisabledException{
		AbstractDeviceObserverStub extra_obStub = new AbstractDeviceObserverStub();
		test_object.attach(extra_obStub);
		E.endConfigurationPhase();
		test_object.endConfigurationPhase();
		test_object.wish_to_checkout(payment);
		
	}
	
	@Test
	public void test_normal_wish() throws OverloadException, DisabledException {
		test_object.endConfigurationPhase();
		E.attach(w_observer);
		E.endConfigurationPhase();
		E.enable();
		test_object.wish_to_checkout(payment);
		assertTrue("this should be true",test_object.isDisabled());
		
	}
	
	@Test
	public void test_has_space() {
		assertFalse(test_object.hasSpace());
		
	}
	
	@Test
	public void test_not_expected_observer() throws OverloadException, DisabledException {
		AbstractDeviceObserverStub extra_obStub = new AbstractDeviceObserverStub();
		test_object.attach(extra_obStub);
		test_object.accept(payment);
	} ///

}
