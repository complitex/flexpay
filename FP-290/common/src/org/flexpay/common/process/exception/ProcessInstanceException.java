package org.flexpay.common.process.exception;

import org.flexpay.common.exception.FlexPayException;

public class ProcessInstanceException extends FlexPayException {

    public ProcessInstanceException(String message) {
        super(message);
    }

    public ProcessInstanceException(String message, String errorKey, Object... params) {
        super(message, errorKey, params);
    }

    public ProcessInstanceException(Throwable cause) {
        super(cause);
    }

    public ProcessInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

}
