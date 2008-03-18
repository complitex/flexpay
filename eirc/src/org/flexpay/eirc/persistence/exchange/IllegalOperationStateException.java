package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;

public class IllegalOperationStateException extends FlexPayException {

	/**
	 * Constructs exception with the specified detail message
	 *
	 * @param message the detail message
	 */
	public IllegalOperationStateException(String message) {
		super(message);
	}
}
