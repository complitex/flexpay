package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;

/**
 * Exception thrown indicates database inconsistencies
 */
public class IllegalRecordsStateException extends FlexPayException {

	/**
	 * Constructs exception with the specified detail message
	 *
	 * @param message the detail message
	 */
	public IllegalRecordsStateException(String message) {
		super(message);
	}
}
