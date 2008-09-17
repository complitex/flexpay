package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class JobConfigurationNotFoundException extends FlexPayException {
    public JobConfigurationNotFoundException(String message) {
        super(message);
    }

    public JobConfigurationNotFoundException(String message, String errorKey, Object... params) {
        super(message, errorKey, params);
    }

    public JobConfigurationNotFoundException(Throwable cause) {
        super(cause);
    }

    public JobConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
