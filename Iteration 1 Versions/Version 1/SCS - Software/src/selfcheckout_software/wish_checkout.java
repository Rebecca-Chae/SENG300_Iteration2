package selfcheckout_software;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.Acceptor;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;

public class wish_checkout extends AbstractDevice<AbstractDeviceObserver> implements Acceptor<AbstractDeviceObserver>{
	
	private ElectronicScale electronic;
	
	
	
	
	public wish_checkout(ElectronicScale e) {
		this.electronic = e;
	}
	
	public void wish_to_checkout(AbstractDeviceObserver payment_method) throws OverloadException, DisabledException  {
		
			this.attach(payment_method);
			while(true) {
				if (!electronic.isDisabled()) {  // keep checking the weight of bagging area, check if the weight has changed
					
					this.accept(payment_method);
					
					if (this.isDisabled()) {
						break;
					}
					else { //this should never been run
						throw new SimulationException("can't receive payment method");
					}
					
				}
				else {
					throw new SimulationException("the weight has changed");
				}
				
			}
			//....might need to call the matched payment method ///
			
	}

	@Override
	public void accept(AbstractDeviceObserver thing) throws OverloadException, DisabledException {
		if(observers.size()==1 && observers.contains(thing)) {
			for(AbstractDeviceObserver A : observers) {
				A.enabled(this);
			}
		}
	}



	@Override
	public boolean hasSpace() { //not used yet
		// TODO Auto-generated method stub
		return false;
	}
	
}
