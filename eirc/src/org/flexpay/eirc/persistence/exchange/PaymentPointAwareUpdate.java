package org.flexpay.eirc.persistence.exchange;

import org.flexpay.orgs.persistence.PaymentPoint;

/**
 * Marker interface for updates that may need a payment point reference
 */
public interface PaymentPointAwareUpdate {

	/**
	 * Do setup payment point for update
	 * 
	 * @param point PaymentPoint
	 */
	void setPoint(PaymentPoint point);
}
