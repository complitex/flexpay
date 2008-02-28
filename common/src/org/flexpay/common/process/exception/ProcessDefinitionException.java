package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class ProcessDefinitionException extends FlexPayException {

    public ProcessDefinitionException(String message) {
        super(message);
    }

    public ProcessDefinitionException(String message, String errorKey, String... params) {
        super(message, errorKey, params);
    }

    public ProcessDefinitionException(Throwable cause) {
        super(cause);
    }

    public ProcessDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
