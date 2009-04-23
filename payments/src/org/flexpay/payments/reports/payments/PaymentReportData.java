package org.flexpay.payments.reports.payments;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Report data holder for payments report
 */
public class PaymentReportData implements Serializable {

	private Date beginDate;
	private Date endDate;
	private Long bankId;
	private String subdivisionName;
	private Long serviceId;
	private String serviceName;
	private BigDecimal payedCacheSumm = BigDecimal.ZERO;
	private BigDecimal payedCachelessSumm = BigDecimal.ZERO;
	private BigDecimal returnedCacheSumm = BigDecimal.ZERO;
	private BigDecimal returnedCachelessSumm = BigDecimal.ZERO;

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

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getSubdivisionName() {
		return subdivisionName;
	}

	public void setSubdivisionName(String subdivisionName) {
		this.subdivisionName = subdivisionName;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public BigDecimal getPayedCacheSumm() {
		return payedCacheSumm;
	}

	public void setPayedCacheSumm(BigDecimal payedCacheSumm) {
		this.payedCacheSumm = payedCacheSumm;
	}

	public BigDecimal getPayedCachelessSumm() {
		return payedCachelessSumm;
	}

	public void setPayedCachelessSumm(BigDecimal payedCachelessSumm) {
		this.payedCachelessSumm = payedCachelessSumm;
	}

	public BigDecimal getReturnedCacheSumm() {
		return returnedCacheSumm;
	}

	public void setReturnedCacheSumm(BigDecimal returnedCacheSumm) {
		this.returnedCacheSumm = returnedCacheSumm;
	}

	public BigDecimal getReturnedCachelessSumm() {
		return returnedCachelessSumm;
	}

	public void setReturnedCachelessSumm(BigDecimal returnedCachelessSumm) {
		this.returnedCachelessSumm = returnedCachelessSumm;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("bankId", bankId).
				append("subdivisionName", subdivisionName).
				append("serviceId", serviceId).
				append("serviceName", serviceName).
				append("payedCacheSumm", payedCacheSumm).
				append("payedCachelessSumm", payedCachelessSumm).
				append("returnedCacheSumm", returnedCacheSumm).
				append("returnedCachelessSumm", returnedCachelessSumm).
				toString();
	}

	public BigDecimal getTotalPayed() {
		BigDecimal summ = payedCacheSumm == null ? BigDecimal.ZERO : payedCacheSumm;
		summ = summ.add(payedCachelessSumm == null ? BigDecimal.ZERO : payedCachelessSumm);
		summ = summ.subtract(returnedCacheSumm == null ? BigDecimal.ZERO : returnedCacheSumm);
		summ = summ.subtract(returnedCachelessSumm == null ? BigDecimal.ZERO : returnedCachelessSumm);
		return summ;
	}
}
