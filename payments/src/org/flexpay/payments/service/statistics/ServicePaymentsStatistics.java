package org.flexpay.payments.service.statistics;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;

public class ServicePaymentsStatistics {

	private Long organizationId;
	private Long serviceId;
	private BigDecimal payedCacheSum = BigDecimal.ZERO;
	private BigDecimal payedCachelessSum = BigDecimal.ZERO;
	private BigDecimal returnedCacheSum = BigDecimal.ZERO;
	private BigDecimal returnedCachelessSum = BigDecimal.ZERO;

	public ServicePaymentsStatistics() {
	}

	public ServicePaymentsStatistics(
			Long organizationId,
			Long serviceId,
			BigDecimal payedCacheSum,
			BigDecimal payedCachelessSum,
			BigDecimal returnedCacheSum,
			BigDecimal returnedCachelessSum) {

		this.organizationId = organizationId;
		this.serviceId = serviceId;
		this.payedCacheSum = payedCacheSum;
		this.payedCachelessSum = payedCachelessSum;
		this.returnedCacheSum = returnedCacheSum;
		this.returnedCachelessSum = returnedCachelessSum;
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

	public BigDecimal getPayedCacheSum() {
		return payedCacheSum;
	}

	public void setPayedCacheSum(BigDecimal payedCacheSum) {
		this.payedCacheSum = payedCacheSum;
	}

	public BigDecimal getPayedCachelessSum() {
		return payedCachelessSum;
	}

	public void setPayedCachelessSum(BigDecimal payedCachelessSum) {
		this.payedCachelessSum = payedCachelessSum;
	}

	public BigDecimal getReturnedCacheSum() {
		return returnedCacheSum;
	}

	public void setReturnedCacheSum(BigDecimal returnedCacheSum) {
		this.returnedCacheSum = returnedCacheSum;
	}

	public BigDecimal getReturnedCachelessSum() {
		return returnedCachelessSum;
	}

	public void setReturnedCachelessSum(BigDecimal returnedCachelessSum) {
		this.returnedCachelessSum = returnedCachelessSum;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("organizationId", organizationId).
				append("serviceId", serviceId).
				append("payedCacheSum", payedCacheSum).
				append("payedCachelessSum", payedCachelessSum).
				append("returnedCacheSum", returnedCacheSum).
				append("returnedCachelessSum", returnedCachelessSum).
				toString();
	}

}
