package org.flexpay.common.exception;

/**
 * Common (parent) exception of FlexPay system
 */
public class FlexPayException extends Exception{
    /**
     * Constructs exception with the specified detail message
     *
     * @param message the detail message
     */
    public FlexPayException(String message) {
        super(message);
    }

    /**
     * Constructs exception from root cause exception.
     *
     * @param cause the cause
     */
    public FlexPayException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs exception from message and root cause exception.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public FlexPayException(String message, Throwable cause) {
        super(message, cause);
    }

}
