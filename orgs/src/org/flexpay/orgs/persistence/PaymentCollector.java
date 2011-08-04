package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.util.Collections;
import java.util.Set;

public class PaymentCollector extends OrganizationInstance<PaymentCollectorDescription, PaymentCollector> {

	private String email;
	private Set<PaymentPoint> paymentPoints = Collections.emptySet();
	private Long tradingDayProcessInstanceId;
	private Time tradingDayBeginTime;
	private Time tradingDayEndTime;

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

	public Long getTradingDayProcessInstanceId() {
		return tradingDayProcessInstanceId;
	}

	public void setTradingDayProcessInstanceId(Long tradingDayProcessInstanceId) {
		this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
	}

	public Time getTradingDayBeginTime() {
		return tradingDayBeginTime;
	}

	public void setTradingDayBeginTime(Time tradingDayBeginTime) {
		this.tradingDayBeginTime = tradingDayBeginTime;
	}

	public Time getTradingDayEndTime() {
		return tradingDayEndTime;
	}

	public void setTradingDayEndTime(Time tradingDayEndTime) {
		this.tradingDayEndTime = tradingDayEndTime;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", id).
                append("status", status).
                append("email", email).
				append("trading day begin time", tradingDayBeginTime).
				append("trading day end time", tradingDayEndTime).
                toString();
    }
}
