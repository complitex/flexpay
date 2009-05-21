package org.flexpay.payments.actions;

import org.flexpay.common.actions.StaticPageAction;
import org.flexpay.payments.persistence.Operation;

public class PaymentPointAwareStaticAction extends StaticPageAction implements PaymentPointAwareAction {

	private Long paymentPointId;
	private Long organizationId;

	// print previously created operation
	private Operation operation = new Operation();

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
