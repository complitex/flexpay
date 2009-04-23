package org.flexpay.payments.service.statistics;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;

public class ServicePaymentsStatistics {

	private Long organizationId;
	private Long serviceId;
	private BigDecimal payedCacheSumm = BigDecimal.ZERO;
	private BigDecimal payedCachelessSumm = BigDecimal.ZERO;
	private BigDecimal returnedCacheSumm = BigDecimal.ZERO;
	private BigDecimal returnedCachelessSumm = BigDecimal.ZERO;

	public ServicePaymentsStatistics() {
	}

	public ServicePaymentsStatistics(
			Long organizationId,
			Long serviceId,
			BigDecimal payedCacheSumm,
			BigDecimal payedCachelessSumm,
			BigDecimal returnedCacheSumm,
			BigDecimal returnedCachelessSumm) {

		this.organizationId = organizationId;
		this.serviceId = serviceId;
		this.payedCacheSumm = payedCacheSumm;
		this.payedCachelessSumm = payedCachelessSumm;
		this.returnedCacheSumm = returnedCacheSumm;
		this.returnedCachelessSumm = returnedCachelessSumm;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
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
				append("organizationId", organizationId).
				append("serviceId", serviceId).
				append("payedCacheSumm", payedCacheSumm).
				append("payedCachelessSumm", payedCachelessSumm).
				append("returnedCacheSumm", returnedCacheSumm).
				append("returnedCachelessSumm", returnedCachelessSumm).
				toString();
	}
}
