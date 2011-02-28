package org.flexpay.common.exception;

/**
 * User do not have certificate
 */
public class CertificateNotFoundException extends FlexPayException {

	/**
	 * Constructs exception with the user name
	 *
	 * @param userName User name
	 */
	public CertificateNotFoundException(String userName) {
		super(getMessage(userName));
	}

	/**
	 * {@inheritDoc}
	 */
	public CertificateNotFoundException(String userName, String errorKey, Object... params) {
		super(getMessage(userName), errorKey, params);
	}

	private static String getMessage(String userName) {
		return "User '" + userName + "' do not have certificate";
	}
}
