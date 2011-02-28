package org.flexpay.common.exception;

/**
 * User`s certificate blocked
 */
public class CertificateBlockedException extends FlexPayException {

	/**
	 * Constructs exception with the user name
	 *
	 * @param userName User name
	 */
	public CertificateBlockedException(String userName) {
		super(getMessage(userName));
	}

	/**
	 * {@inheritDoc}
	 */
	public CertificateBlockedException(String userName, String errorKey, Object... params) {
		super(getMessage(userName), errorKey, params);
	}

	private static String getMessage(String userName) {
		return "Certificate of user '" + userName + "' blocked ";
	}
}
