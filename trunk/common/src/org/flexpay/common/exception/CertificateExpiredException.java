package org.flexpay.common.exception;

/**
 * User`s certificate expired
 */
public class CertificateExpiredException extends FlexPayException {

	/**
	 * Constructs exception with the user name
	 *
	 * @param userName User name
	 */
	public CertificateExpiredException(String userName) {
		super(getMessage(userName));
	}

	public CertificateExpiredException(String userName, String errorKey, Object... params) {
		super(getMessage(userName), errorKey, params);
	}

	private static String getMessage(String userName) {
		return "Certificate for user '" + userName + "' expired";
	}
}
