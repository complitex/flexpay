package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class JobInstantiationException extends FlexPayException {
    public JobInstantiationException(String message) {
        super(message);
    }

    public JobInstantiationException(String message, String errorKey, Object... params) {
        super(message, errorKey, params);
    }

    public JobInstantiationException(Throwable cause) {
        super(cause);
    }

    public JobInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
