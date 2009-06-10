package org.flexpay.orgs.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Stub;

import java.util.Collections;
import java.util.Set;

public class PaymentsCollector extends OrganizationInstance<PaymentsCollectorDescription, PaymentsCollector> {

	private Set<PaymentPoint> paymentPoints = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public PaymentsCollector() {
	}

	public PaymentsCollector(@NotNull Long id) {
		super(id);
	}

	public PaymentsCollector(@NotNull Stub<PaymentsCollector> stub) {
		super(stub.getId());
	}

	public Set<PaymentPoint> getPaymentPoints() {
		return paymentPoints;
	}

	public void setPaymentPoints(Set<PaymentPoint> paymentPoints) {
		this.paymentPoints = paymentPoints;
	}
}
