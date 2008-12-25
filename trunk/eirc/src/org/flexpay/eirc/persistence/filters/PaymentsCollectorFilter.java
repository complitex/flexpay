package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.PaymentsCollector;

import java.util.List;
import java.util.Collections;

public class PaymentsCollectorFilter extends PrimaryKeyFilter<PaymentsCollector> {

	private List<PaymentsCollector> collectors = Collections.emptyList();

	public PaymentsCollectorFilter() {
		super(-1L);
	}

	public List<PaymentsCollector> getCollectors() {
		return collectors;
	}

	public void setCollectors(List<PaymentsCollector> collectors) {
		this.collectors = collectors;
	}
}
