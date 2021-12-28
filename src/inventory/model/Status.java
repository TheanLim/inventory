package inventory.model;

import java.util.List;

/**
 * A {@code Status} object is used to record a status code, a transaction output,
 * and possibly an error resulting from a CRUD operation by an {@link IModel} implementation.
 * <br> The Status Code is either {@code SUCCESS} or {@code FAIL}.
 * @author thean
 *
 */
public class Status {
	/**
	 * Describing the status a CRUD transaction
	 * @author thean
	 *
	 */
	public enum StatusCode{
		SUCCESS, FAIL
	}
	
	private StatusCode statusCode;
	private List<?> transactionOutput;
	private Throwable error;
	private String errorMessage;
	
	/**
	 * Constructs a {@code Status} object
	 * @param status the status a CRUD transaction
	 */
	public Status(StatusCode status) {
		this.statusCode = status;
	}
	
	/**
	 * Constructs a {@code Status} object
	 * @param status the status a CRUD transaction
	 * @param transactionOutput the output of a CRUD transaction
	 */
	public Status(StatusCode status, List<?> transactionOutput) {
		this.statusCode = status;
		this.transactionOutput = transactionOutput;
	}
	
	/**
	 * Constructs a {@code Status} object
	 * @param status the status a CRUD transaction
	 * @param error the error thrown by a CRUD transaction
	 */
	public Status(StatusCode status, Throwable error) {
		this.statusCode = status;
		this.error = error;
	}
	
	/**
	 * Constructs a {@code Status} object
	 * @param status the status a CRUD transaction
	 * @param errorMessage the errorMessage thrown by a CRUD transaction
	 */
	public Status(StatusCode statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Get the Status code of a CRUD transaction
	 * @return a {@code StatusCode}: either {@code SUCCESS} or {@code FAIL}.
	 */
	public StatusCode getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Get the CRUD transaction output
	 * @return the CRUD transaction output in a list.
	 */
	public List<?> getTransactionOutput() {
		return transactionOutput;
	}
	
	/**
	 * Get the error thrown by a CRUD transaction
	 * @return the error thrown by a CRUD transaction
	 */
	public Throwable getError() {
		return error;
	}
	
	/**
	 * Get the error message thrown by a CRUD transaction
	 * @return the error message thrown by a CRUD transaction
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
