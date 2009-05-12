package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.orgs.persistence.PaymentPoint;

import java.util.Collections;
import java.util.List;

public class PaymentPointsFilter extends PrimaryKeyFilter<PaymentPoint> {

	private List<PaymentPoint> points = Collections.emptyList();

	public PaymentPointsFilter() {
		super(-1L);
	}

	public List<PaymentPoint> getPoints() {
		return points;
	}

	public void setPoints(List<PaymentPoint> points) {
		this.points = points;
	}
}