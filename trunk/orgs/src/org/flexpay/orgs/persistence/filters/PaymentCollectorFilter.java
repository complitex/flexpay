package org.flexpay.orgs.persistence.filters;

import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;

public class PaymentCollectorFilter
		extends OrganizationInstanceFilter<PaymentCollectorDescription, PaymentCollector> {

	public PaymentCollectorFilter() {
		super(-1L);
	}

	public PaymentCollectorFilter(Long selectedId) {
		super(selectedId);
	}

}
