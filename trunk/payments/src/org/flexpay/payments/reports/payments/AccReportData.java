package org.flexpay.payments.reports.payments;

import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

public abstract class AccReportData {

	protected Date beginDate;
	protected Date endDate;
	protected String paymentCollectorOrgName;
	protected String paymentCollectorOrgAddress;
	protected String accountantFio;
	protected Date creationDate;

    public abstract JRDataSource getDataSource();

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

	public String getPaymentCollectorOrgAddress() {
		return paymentCollectorOrgAddress;
	}

	public void setPaymentCollectorOrgAddress(String paymentCollectorOrgAddress) {
		this.paymentCollectorOrgAddress = paymentCollectorOrgAddress;
	}

	public String getAccountantFio() {
		return accountantFio;
	}

	public void setAccountantFio(String accountantFio) {
		this.accountantFio = accountantFio;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getPaymentCollectorOrgName() {
		return paymentCollectorOrgName;
	}

	public void setPaymentCollectorOrgName(String paymentCollectorOrgName) {
		this.paymentCollectorOrgName = paymentCollectorOrgName;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("beginDate", beginDate).
                append("endDate", endDate).
                append("paymentCollectorOrgName", paymentCollectorOrgName).
                append("paymentCollectorOrgAddress", paymentCollectorOrgAddress).
                append("accountantFio", accountantFio).
                append("creationDate", creationDate).
                toString();
    }
}
