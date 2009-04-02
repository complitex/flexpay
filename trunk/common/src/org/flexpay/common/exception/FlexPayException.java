package org.flexpay.common.exception;

import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Common (parent) exception of FlexPay system, contains localisable error key
 */
public class FlexPayException extends Exception {

	/**
	 * I18n error key name
	 */
	private String errorKey;
	/**
	 * parameters used to build error message
	 */
	private String[] params;

	/**
	 * Constructs exception with the specified detail message
	 *
	 * @param message the detail message
	 */
	public FlexPayException(@NonNls String message) {
		super(message);
	}

	/**
	 * Constructs exception with specified detail message, error code key and optional error message parameters
	 *
	 * @param message  the detail message
	 * @param errorKey localization error message key
	 * @param params   optional localization error message parameters
	 */
	public FlexPayException(@NonNls String message, String errorKey, Object... params) {
		super(message);
		this.errorKey = errorKey;
		this.params = convert(params);
	}

	/**
	 * Constructs exception with specified detail message, error code key and optional error message parameters
	 *
	 * @param message  the detail message
	 * @param cause	the cause
	 * @param errorKey localization error message key
	 * @param params   optional localization error message parameters
	 */
	public FlexPayException(@NonNls String message, Throwable cause, String errorKey, Object... params) {
		super(message, cause);
		this.errorKey = errorKey;
		this.params = convert(params);
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

	/**
	 * Getter for property 'errorKey'.
	 *
	 * @return Value for property 'errorKey'.
	 */
	@Nls
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * Setter for property 'errorKey'.
	 *
	 * @param errorKey Value to set for property 'errorKey'.
	 */
	public void setErrorKey(@Nls String errorKey) {
		this.errorKey = errorKey;
	}

	/**
	 * Getter for property 'params'.
	 *
	 * @return Value for property 'params'.
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * Setter for property 'params'.
	 *
	 * @param params Value to set for property 'params'.
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	/**
	 * Convert objects array to string parameters
	 *
	 * @param objects Objects to convert
	 * @return Array of string parameters
	 */
	private String[] convert(Object[] objects) {
		String[] res = new String[objects.length];
		int n = 0;
		for (Object obj : objects) {

			res[n] = obj instanceof Date ? DateUtil.format((Date) obj) : String.valueOf(obj);
			n++;
		}

		return res;
	}

	public void debug(Logger log) {
		if (log.isDebugEnabled()) {
			log.debug(getMessage(), this);
		}
	}

	public void debug(Logger log, String format, Object ... params) {
		if (log.isDebugEnabled()) {
			log.debug(getMessage(), this);
			log.debug(format, params);
		}
	}

	public void info(Logger log) {
		if (log.isInfoEnabled()) {
			log.info(getMessage(), this);
		}
	}

	public void info(Logger log, String format, Object ... params) {
		if (log.isInfoEnabled()) {
			log.info(getMessage(), this);
			log.info(format, params);
		}
	}

	public void error(Logger log, String format, Object ... params) {
		log.error(getMessage(), this);
		log.error(format, params);
	}
}
