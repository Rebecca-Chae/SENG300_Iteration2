package selfcheckout_software;
import java.util.ArrayList;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.Acceptor;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;
import selfcheckout_software.wish_checkout;
import selfcheckout_test.test_wish_to_checkout;

public class baggingItem implements ElectronicScaleObserver{
	
	
	
	private ElectronicScale electronic;
	
	private ArrayList<Item> items = new ArrayList<>();
	private double counter=0;

	public baggingItem(ElectronicScale e) {
		this.electronic = e
;	}
	
	public void bagItem(Item i, ElectronicScale e) throws OverloadException, SimulationException {
		try {
			e.add(i);
			
			double current = e.getCurrentWeight();
			double itemWeight = i.getWeight();
			counter+=itemWeight;

			System.out.println(current);

			if (counter!=current) {
				throw new SimulationException("Please put the correct items in the bagging area");
			}
		}catch(OverloadException o) {
			throw new OverloadException();
		}
		
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
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void overload(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	

}
