package org.flexpay.eirc.persistence;

import java.util.Collections;
import java.util.Set;

public class PaymentsCollector extends OrganizationInstance<PaymentsCollectorDescription, PaymentsCollector> {

	private Set<PaymentPoint> paymentPoints = Collections.emptySet();

	public Set<PaymentPoint> getPaymentPoints() {
		return paymentPoints;
	}

	public void setPaymentPoints(Set<PaymentPoint> paymentPoints) {
		this.paymentPoints = paymentPoints;
	}
}
