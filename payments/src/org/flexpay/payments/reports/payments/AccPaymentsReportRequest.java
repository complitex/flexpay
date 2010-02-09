package org.flexpay.payments.reports.payments;

import java.util.Date;
import java.util.Locale;

public class AccPaymentsReportRequest {

	public static final int DETAILS_LEVEL_PAYMENT_POINT = 1;
	public static final int DETAILS_LEVEL_CASHBOX = 2;
	public static final int DETAILS_LEVEL_PAYMENT = 3;

	private int paymentStatus;
	private int detailsLevel;
	private String format;
	private Date beginDate;
	private Date endDate;
	private Long paymentPointId;
	private Long cashboxId;
	private Locale locale;

	public int getDetailsLevel() {
		return detailsLevel;
	}

	public void setDetailsLevel(int detailsLevel) {
		this.detailsLevel = detailsLevel;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
