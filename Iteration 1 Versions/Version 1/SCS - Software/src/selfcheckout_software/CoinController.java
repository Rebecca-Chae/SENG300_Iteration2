package selfcheckout_software;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.*;

// logic for the use case "customer wants to pay with coin" by implementing the CoinValidatorObserver
// and adding value of a coin to a local variable availableFunds if the proper event occurs (a valid coin is detected). 
public class CoinController implements CoinValidatorObserver, CoinStorageUnitObserver {
	
	private Coin c;
	private BigDecimal availableFunds = new BigDecimal(0);
	private Boolean storageFull = false;
	
	public CoinController(Coin c) {
		this.c = c;
	}
	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	// validator determines the coin is valid, add value of coin to sum
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		if(!storageFull) {
			availableFunds = availableFunds.add(value);
		}
	}

	// validator determines the coin is not valid, do nothing
	@Override
	public void invalidCoinDetected(CoinValidator validator) {	
	}
	
	public BigDecimal getAvailableFunds() {
		return availableFunds;
	}
	
	public Boolean getStorageFull() {
		return storageFull;
	}
	
	public void setStorageFull(Boolean value) {
		storageFull = value;
	}

	
	@Override
	public void coinsFull(CoinStorageUnit unit) {
		storageFull = true;
	}

	@Override
	public void coinAdded(CoinStorageUnit unit) {
	}

	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
	}

	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		storageFull = false;
	}

}
