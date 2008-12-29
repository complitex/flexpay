package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.PaymentsCollectorDescription;

import java.util.List;
import java.util.Collections;

public class PaymentsCollectorFilter
		extends OrganizationInstanceFilter<PaymentsCollectorDescription, PaymentsCollector> {

	public PaymentsCollectorFilter() {
		super(-1L);
	}
}
