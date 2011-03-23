package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class ProcessInstanceException extends FlexPayException {

    public ProcessInstanceException(String message, String errorKey, Object... params) {
        super(message, errorKey, params);
    }

	/**
	 * Constructs exception with specified detail message, error code key and optional error message parameters
	 *
	 * @param message  the detail message
	 * @param cause	the cause
	 * @param errorKey localization error message key
	 * @param params   optional localization error message parameters
	 */
	public ProcessInstanceException(String message, Throwable cause, String errorKey, Object... params) {
		super(message, cause, errorKey, params);
	}
}
