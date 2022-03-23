package ca.ucalgary.seng300.selfcheckoutP1;

/**
 * Signals that the banknote/coin payment is not enough amount
 */
@SuppressWarnings("serial")
public class CashPaymentException extends Exception {

	/**
	 * Create an exception without an error message.
	 */
	public CashPaymentException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public CashPaymentException(String message) {
		super(message);
	}
}
