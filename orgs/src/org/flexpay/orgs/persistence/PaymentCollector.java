package org.flexpay.orgs.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Stub;

import java.util.Collections;
import java.util.Set;

public class PaymentCollector extends OrganizationInstance<PaymentCollectorDescription, PaymentCollector> {

	private String email;
	private Set<PaymentPoint> paymentPoints = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public PaymentCollector() {
	}

	public PaymentCollector(@NotNull Long id) {
		super(id);
	}

	public PaymentCollector(@NotNull Stub<PaymentCollector> stub) {
		super(stub.getId());
	}

	public Set<PaymentPoint> getPaymentPoints() {
		return paymentPoints;
	}

	public void setPaymentPoints(Set<PaymentPoint> paymentPoints) {
		this.paymentPoints = paymentPoints;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
