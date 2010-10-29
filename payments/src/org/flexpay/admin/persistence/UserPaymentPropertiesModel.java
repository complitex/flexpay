package org.flexpay.admin.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UserPaymentPropertiesModel {

	private String username;
	private Long paymentPointId;
	private Long paymentCollectorId;
	private Long cashboxId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
	}

	public Long getPaymentCollectorId() {
		return paymentCollectorId;
	}

	public void setPaymentCollectorId(Long paymentCollectorId) {
		this.paymentCollectorId = paymentCollectorId;
	}

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

	public static UserPaymentPropertiesModel getInstance() {
		return new UserPaymentPropertiesModel();
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("username", username).
                append("paymentPointId", paymentPointId).
                append("paymentCollectorId", paymentCollectorId).
                append("cashboxId", cashboxId).
                toString();
    }
}
