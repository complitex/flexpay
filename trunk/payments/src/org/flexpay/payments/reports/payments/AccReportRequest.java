package org.flexpay.payments.reports.payments;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;
import java.util.Locale;

public abstract class AccReportRequest {

    public static final int DETAILS_LEVEL_PAYMENT_POINT = 1;
    public static final int DETAILS_LEVEL_CASHBOX = 2;
    public static final int DETAILS_LEVEL_PAYMENT = 3;

    protected int paymentStatus;
    protected String format;
    protected Date beginDate;
    protected Date endDate;
    protected Long paymentCollectorId;
    protected Locale locale;

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public Long getPaymentCollectorId() {
        return paymentCollectorId;
    }

    public void setPaymentCollectorId(Long paymentCollectorId) {
        this.paymentCollectorId = paymentCollectorId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("format", format).
                append("beginDate", beginDate).
                append("endDate", endDate).
                append("paymentCollectorId", paymentCollectorId).
                append("locale", locale).
                toString();
    }

}
