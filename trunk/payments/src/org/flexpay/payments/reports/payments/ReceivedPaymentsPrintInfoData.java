package org.flexpay.payments.reports.payments;

import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;

/**
 * Represents information of received payments report
 */
public class ReceivedPaymentsPrintInfoData {

	private Date creationDate;
	private Date beginDate;
	private Date endDate;
	private String paymentPointName;
	private String paymentPointAddress;
	private String cashierFio;
	private Long totalPaymentsCount;
	private BigDecimal totalPaymentsSumm;
	private List<OperationPrintInfo> operationDetailses = CollectionUtils.list();
	private Map<Integer, Integer> serviceTypePaymentsCounts; // maps service type code to number of payments for it
	private Map<Integer, BigDecimal> serviceTypePaymentsTotals; // maps service type code to total payments summ for it

	public Long getTotalPaymentsCount() {
		return totalPaymentsCount;
	}

	public void setTotalPaymentsCount(Long totalPaymentsCount) {
		this.totalPaymentsCount = totalPaymentsCount;
	}

	public BigDecimal getTotalPaymentsSumm() {
		return totalPaymentsSumm;
	}

	public void setTotalPaymentsSumm(BigDecimal totalPaymentsSumm) {
		this.totalPaymentsSumm = totalPaymentsSumm;
	}

	public String getCashierFio() {
		return cashierFio;
	}

	public void setCashierFio(String cashierFio) {
		this.cashierFio = cashierFio;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public String getPaymentPointName() {
		return paymentPointName;
	}

	public void setPaymentPointName(String paymentPointName) {
		this.paymentPointName = paymentPointName;
	}

	public String getPaymentPointAddress() {
		return paymentPointAddress;
	}

	public void setPaymentPointAddress(String paymentPointAddress) {
		this.paymentPointAddress = paymentPointAddress;
	}

	public List<OperationPrintInfo> getOperationDetailses() {
		return operationDetailses;
	}

	public void setOperationDetailses(List<OperationPrintInfo> operationDetailses) {
		this.operationDetailses = operationDetailses;
	}

	public Map<Integer, Integer> getServiceTypePaymentsCounts() {
		return serviceTypePaymentsCounts;
	}

	public void setServiceTypePaymentsCounts(Map<Integer, Integer> serviceTypePaymentsCounts) {
		this.serviceTypePaymentsCounts = serviceTypePaymentsCounts;
	}

	public Map<Integer, BigDecimal> getServiceTypePaymentsTotals() {
		return serviceTypePaymentsTotals;
	}

	public void setServiceTypePaymentsTotals(Map<Integer, BigDecimal> serviceTypePaymentsTotals) {
		this.serviceTypePaymentsTotals = serviceTypePaymentsTotals;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("creationDate", creationDate).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("paymentPointName", paymentPointName).
				append("paymentPointAddress", paymentPointAddress).
				append("cashierFio", cashierFio).
				append("totalPaymentsCount", totalPaymentsCount).
				append("totalPaymentsSumm", totalPaymentsSumm).
				toString();
		}

	/**
	 * Represents printable information about an operation
	 */
	public static class OperationPrintInfo {

		private Long operationId;
		private String payerFio;
		private BigDecimal summ;
		private Map<Integer, BigDecimal> servicePayments; // maps service code to payment for the service inthe operation

		public Long getOperationId() {
			return operationId;
		}

		public void setOperationId(Long operationId) {
			this.operationId = operationId;
		}

		public String getPayerFio() {
			return payerFio;
		}

		public void setPayerFio(String payerFio) {
			this.payerFio = payerFio;
		}

		public BigDecimal getSumm() {
			return summ;
		}

		public void setSumm(BigDecimal summ) {
			this.summ = summ;
		}

		public Map<Integer, BigDecimal> getServicePayments() {
			return servicePayments;
		}

		public void setServicePayments(Map<Integer, BigDecimal> servicePayments) {
			this.servicePayments = servicePayments;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).
					append("operationId", operationId).
					append("payerFio", payerFio).
					append("summ", summ).
					toString();
		}
	}
}
