package selfcheckout_software;
import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteStorageUnitObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteValidatorObserver;

public class BanknoteController implements BanknoteValidatorObserver, BanknoteStorageUnitObserver{

	private long availableFunds;
	private int validBanknotes = 0;
	private int invalidBanknotes = 0;
	private boolean isFull = false;
	
	
	/*
	 * Constructor for the BanknoteController class
	 * Simulates the use case for the user entering a banknote
	 * 
	 * @param SelfCheckoutStation s
	 * 	The self checkout station which this class will be listening too
	 * */
	public BanknoteController(SelfCheckoutStation s) {
		
		// Attaching this object to listen to the banknoteStorage and banknoteValidtor objects
		s.banknoteValidator.attach(this);
		s.banknoteStorage.attach(this);
		availableFunds = 0;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}

	/*
	 * Implementing validBanknoteDetected function from the interface of BanknoteValidatorObserver
	 * */
	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		
		// If the storage unit for the banknote is not full
		// we add the dollar value to availableFunds
		if(!isFull) {
			availableFunds+=value;
			validBanknotes++;
		}
		
	}
	
	/*
	 * Implementing invalidBanknoteDetected function from the interface of BanknoteValidatorObserver
	 * */
	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		invalidBanknotes++;
	}

	// Used to determine if the storage unit for the banknote is full
	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {
		isFull = true;
		
	}

	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {}

	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {}

	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		isFull = false;
	}
	
	
	/*
	 * Checking it the current availableFunds is sufficient for purchase
	 * */
	public boolean hasSufficientFunds(BigDecimal price) {
		return new BigDecimal(availableFunds).compareTo(price)>=0;
	}
	
	// Getters and setters for the current funds of the customer, and the number
	// of valid and invalid banknotes
	
	public long getCurrentFunds() {
		return availableFunds;
	}
	
	public int getValidBanknotes() {
		return validBanknotes;
	}
	
	public int getInvalidBanknotes() {
		return invalidBanknotes;
	}
	
	
}
