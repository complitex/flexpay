package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class JobClassNotFoundException extends FlexPayException {
    public JobClassNotFoundException(String message) {
        super(message);
    }

    public JobClassNotFoundException(String message, String errorKey, String... params) {
        super(message, errorKey, params);
    }

    public JobClassNotFoundException(Throwable cause) {
        super(cause);
    }

    public JobClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
