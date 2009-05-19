package org.flexpay.payments.actions;

import org.flexpay.common.actions.StaticPageAction;

public class PaymentPointAwareStaticAction extends StaticPageAction implements PaymentPointAwareAction {

	private String paymentPointId;
	private String organizationId;

	public void setPaymentPointId(String paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointId() {
		return paymentPointId;
	}
	
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
}
