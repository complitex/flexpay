package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.orgs.persistence.PaymentPoint;

import java.util.Collections;
import java.util.List;

public class PaymentPointFilter extends PrimaryKeyFilter<PaymentPoint> {

	private List<PaymentPoint> points = Collections.emptyList();

	public PaymentPointFilter() {
		super(-1L);
	}

	public List<PaymentPoint> getPoints() {
		return points;
	}

	public void setPoints(List<PaymentPoint> points) {
		this.points = points;
		if (!containsSuchId(points, getSelectedId())) {
			setSelectedId(getDefaultId());
		}
	}

	private boolean containsSuchId(List<PaymentPoint> points, Long id) {

		for (PaymentPoint paymentPoint : points) {
			if (paymentPoint.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	public PaymentPoint getSelected() {
		for (PaymentPoint point : points) {
			if (point.getId().equals(getSelectedId())) {
				return point;
			}
		}

		return null;
	}

}
