package org.flexpay.eirc.persistence.exchange;

public interface DelayedUpdateVisitor {

	/**
	 * Do visit of update
	 *
	 * @param update DelayedUpdate  to visit
	 */
	void apply(DelayedUpdate update);
}
