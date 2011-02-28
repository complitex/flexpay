package org.flexpay.common.exception;

/**
 * Invalid verify user signature
 */
public class InvalidVerifySignatureException extends FlexPayException {

	public InvalidVerifySignatureException(String userName) {
		super(getMessage(userName));
	}

	public InvalidVerifySignatureException(String userName, String errorKey, Object... params) {
		super(getMessage(userName), errorKey, params);
	}

	public InvalidVerifySignatureException(String userName, Throwable cause, String errorKey, Object... params) {
		super(getMessage(userName), cause, errorKey, params);
	}

	public InvalidVerifySignatureException(String userName, Throwable cause) {
		super(getMessage(userName), cause);
	}

	private static String getMessage(String userName) {
		return "Failed signature from user '" + userName + "'";
	}
}
