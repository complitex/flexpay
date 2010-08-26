package org.flexpay.payments.reports.payments;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AccPaymentsReportRequest extends AccReportRequest {

	private int detailsLevel;
	private Long paymentPointId;
	private Long cashboxId;

	public int getDetailsLevel() {
		return detailsLevel;
	}

	public void setDetailsLevel(int detailsLevel) {
		this.detailsLevel = detailsLevel;
	}

	public Long getPaymentPointId() {
		return paymentPointId;
	}

	public void setPaymentPointId(Long paymentPointId) {
		this.paymentPointId = paymentPointId;
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
                append("format", format).
                append("beginDate", beginDate).
                append("endDate", endDate).
                append("paymentCollectorId", paymentCollectorId).
                append("locale", locale).
                append("paymentStatus", paymentStatus).
                append("detailsLevel", detailsLevel).
                append("paymentPointId", paymentPointId).
                append("cashboxId", cashboxId).
                toString();
    }
}
