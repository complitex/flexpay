package org.flexpay.payments.util.config;

import org.flexpay.ab.util.config.AbUserPreferences;

public class PaymentsUserPreferences extends AbUserPreferences {

	private Long paymentPointId;
	private Long paymentCollectorId;
	private Long cashBoxId;

	protected PaymentsUserPreferences() {
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public String getPaymentPointIdStr() {
		return paymentPointId == null ? "0" : paymentPointId.toString();
	}

	public String getPaymentCollectorIdStr() {
		return paymentCollectorId == null ? "0" : paymentCollectorId.toString();
	}

	public Long getPaymentCollectorId() {
		return paymentCollectorId;
	}

	public void setPaymentCollectorId(Long paymentCollectorId) {
		this.paymentCollectorId = paymentCollectorId;
	}

	public String getCashBoxIdStr() {
		return cashBoxId == null ? "0" : cashBoxId.toString();
	}

	public Long getCashBoxId() {
		return cashBoxId;
	}

	public void setCashBoxId(Long cashBoxId) {
		this.cashBoxId = cashBoxId;
	}
}
