package org.flexpay.payments.util.config;

import org.flexpay.ab.util.config.AbUserPreferences;

public class PaymentsUserPreferences extends AbUserPreferences {

	private Long paymentPointId;

	protected PaymentsUserPreferences() {
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointIdStr() {
		return paymentPointId == null ? "" : paymentPointId.toString();
	}
}
