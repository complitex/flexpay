package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

/**
 * Perform actual persistence storage update after operation completes
 */
public interface DelayedUpdate {

	/**
	 * Perform storage update
	 *
	 * @throws FlexPayException if operation fails
	 * @throws FlexPayExceptionContainer if operation fails
	 */
	void doUpdate() throws FlexPayException, FlexPayExceptionContainer;
}
