package org.flexpay.orgs.persistence.filters;

import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;

public class PaymentsCollectorFilter
		extends OrganizationInstanceFilter<PaymentsCollectorDescription, PaymentsCollector> {

	public PaymentsCollectorFilter() {
		super(-1L);
	}

	public PaymentsCollectorFilter(Long selectedId) {
		super(selectedId);
	}

}
