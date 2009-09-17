package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;

public interface DelayedUpdateVisitor {

	/**
	 * Do visit of update
	 *
	 * @param update DelayedUpdate  to visit
	 */
	void apply(DelayedUpdate update) throws FlexPayException;
}
