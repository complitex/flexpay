package org.flexpay.payments.actions;

import org.flexpay.common.actions.StaticPageAction;

public class PaymentPointAwareStaticAction extends StaticPageAction implements PaymentPointAwareAction {

	private Long paymentPointId;
	private Long organizationId;

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
}
