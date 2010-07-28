package org.flexpay.payments.reports.payments;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents information of received payments report
 */
public class PaymentsPrintInfoData {

	private static final int SERVICE_TYPE_KVARTPLATA = 1;
	private static final int SERVICE_TYPE_DOGS = 2;
	private static final int SERVICE_TYPE_GARAGE = 3;
	private static final int SERVICE_TYPE_WARMING = 4;
	private static final int SERVICE_TYPE_HOT_WATER = 7;
	private static final int SERVICE_TYPE_COLD_WATER = 6;
	private static final int SERVICE_TYPE_SEWER = 13; // TODO: FIXME find proper one

	private Date creationDate;
	private Date beginDate;
	private Date endDate;
	private String paymentCollectorOrgName;
	private String paymentPointName;
	private String paymentPointAddress;
	private String cashierFio;
	private List<OperationPrintInfo> operationDetailses = CollectionUtils.list();

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

	public String getPaymentCollectorOrgName() {
		return paymentCollectorOrgName;
	}

	public void setPaymentCollectorOrgName(String paymentCollectorOrgName) {
		this.paymentCollectorOrgName = paymentCollectorOrgName;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("creationDate", creationDate).
				append("beginDate", beginDate).
				append("endDate", endDate).
				append("paymentPointName", paymentPointName).
				append("paymentPointAddress", paymentPointAddress).
				append("cashierFio", cashierFio).
				toString();
	}

	/**
	 * Represents printable information about an operation
	 */
	public static class OperationPrintInfo {

		private Long operationId;
		private String payerFio;
		private BigDecimal sum;
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

		public BigDecimal getSum() {
			return sum;
		}

		public void setSum(BigDecimal sum) {
			this.sum = sum;
		}

		public Map<Integer, BigDecimal> getServicePayments() {
			return servicePayments;
		}

		public void setServicePayments(Map<Integer, BigDecimal> servicePayments) {
			this.servicePayments = servicePayments;
		}

		private BigDecimal getServicePayment(Integer code) {
			BigDecimal payment = servicePayments.get(code);
			return payment != null ? payment : new BigDecimal("0.00");
		}

		public BigDecimal getPaymentKvartplata() {
			return getServicePayment(SERVICE_TYPE_KVARTPLATA);
		}

		public BigDecimal getPaymentDogs() {
			return getServicePayment(SERVICE_TYPE_DOGS);
		}

		public BigDecimal getPaymentGarage() {
			return getServicePayment(SERVICE_TYPE_GARAGE);
		}

		public BigDecimal getPaymentWarming() {
			return getServicePayment(SERVICE_TYPE_WARMING);
		}

		public BigDecimal getPaymentHotWater() {
			return getServicePayment(SERVICE_TYPE_HOT_WATER);
		}

		public BigDecimal getPaymentColdWater() {
			return getServicePayment(SERVICE_TYPE_COLD_WATER);
		}

		public BigDecimal getPaymentSewer() {
			return getServicePayment(SERVICE_TYPE_SEWER);
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).
					append("operationId", operationId).
					append("payerFio", payerFio).
					append("sum", sum).
					toString();
		}
	}
}
