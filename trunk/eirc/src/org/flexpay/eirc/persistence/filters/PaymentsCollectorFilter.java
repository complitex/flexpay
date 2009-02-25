package org.flexpay.eirc.persistence.filters;

import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.PaymentsCollectorDescription;

public class PaymentsCollectorFilter
		extends OrganizationInstanceFilter<PaymentsCollectorDescription, PaymentsCollector> {

	public PaymentsCollectorFilter() {
		super(-1L);
	}
}
