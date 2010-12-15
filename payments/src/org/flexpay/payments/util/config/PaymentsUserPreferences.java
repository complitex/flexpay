package org.flexpay.payments.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.config.AbUserPreferences;

public class PaymentsUserPreferences extends AbUserPreferences {

	private Long paymentPointId;
	private Long paymentCollectorId;
	private Long cashboxId;

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

	public String getCashboxIdStr() {
		return cashboxId == null ? "0" : cashboxId.toString();
	}

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                appendSuper(super.toString()).
                append("paymentPointId", paymentPointId).
                append("paymentCollectorId", paymentCollectorId).
                append("cashboxId", cashboxId).
                toString();
    }
}
