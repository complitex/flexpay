package org.flexpay.eirc.process.quittance.report;

import org.flexpay.eirc.persistence.ServiceType;

/**
 * Service totals of a subservice
 */
public class SubServiceTotals extends ServiceTotalsBase {

	public SubServiceTotals(ServiceType serviceType) {
		super(serviceType);
	}
}
