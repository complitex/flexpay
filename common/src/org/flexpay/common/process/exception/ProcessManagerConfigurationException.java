package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class ProcessManagerConfigurationException extends FlexPayException {
    public ProcessManagerConfigurationException(String message) {
        super(message);
    }

    public ProcessManagerConfigurationException(String message, String errorKey, Object... params) {
        super(message, errorKey, params);
    }

    public ProcessManagerConfigurationException(Throwable cause) {
        super(cause);
    }

    public ProcessManagerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
