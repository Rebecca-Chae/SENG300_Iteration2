package ca.ucalgary.seng300.selfcheckoutP1;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

public class BanknoteSlotObs implements BanknoteSlotObserver {

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void banknoteInserted(BanknoteSlot slot) {
		// TODO Auto-generated method stub

	}

	@Override
	// assume that customer re-insert the ejected banknote into the machine when 
	// there is an ejected banknote
	public void banknoteEjected(BanknoteSlot slot) {
		Banknote ejected = slot.removeDanglingBanknote();

		// print out the situation
		System.out.println("Situation: Remove the ejected banknote " + ejected);
	}

	@Override
	public void banknoteRemoved(BanknoteSlot slot) {
		// TODO Auto-generated method stub

	}

}
