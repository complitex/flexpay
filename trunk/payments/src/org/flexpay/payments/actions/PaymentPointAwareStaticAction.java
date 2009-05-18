package org.flexpay.payments.actions;

import org.flexpay.common.actions.StaticPageAction;

public class PaymentPointAwareStaticAction extends StaticPageAction implements PaymentPointAwareAction {

	private String paymentPointId;

	public void setPaymentPointId(String paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointId() {
		return paymentPointId;
	}
}
