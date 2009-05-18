package org.flexpay.payments.reports.payments;

import java.io.Serializable;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.math.BigDecimal;

public class PaymentPrintForm implements Serializable {

	private Date operationDate;
	private String organizationName;
	private String quittanceNumber;
	private String cashierFIO;
	private BigDecimal total;
	private String totalSpelling;
	private BigDecimal inputSumm;
	private BigDecimal changeSumm;

	private List<PaymentDetails> detailses = Collections.emptyList();

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getQuittanceNumber() {
		return quittanceNumber;
	}

	public void setQuittanceNumber(String quittanceNumber) {
		this.quittanceNumber = quittanceNumber;
	}

	public String getCashierFIO() {
		return cashierFIO;
	}

	public void setCashierFIO(String cashierFIO) {
		this.cashierFIO = cashierFIO;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getTotalSpelling() {
		return totalSpelling;
	}

	public void setTotalSpelling(String totalSpelling) {
		this.totalSpelling = totalSpelling;
	}

	public BigDecimal getInputSumm() {
		return inputSumm;
	}

	public void setInputSumm(BigDecimal inputSumm) {
		this.inputSumm = inputSumm;
	}

	public BigDecimal getChangeSumm() {
		return changeSumm;
	}

	public void setChangeSumm(BigDecimal changeSumm) {
		this.changeSumm = changeSumm;
	}

	public List<PaymentDetails> getDetailses() {
		return detailses;
	}

	public void setDetailses(List<PaymentDetails> detailses) {
		this.detailses = detailses;
	}

	public static class PaymentDetails implements Serializable {

		private String address;
		private String fio;
		private String accountNumber;
		private String serviceName;
		private String serviceProviderName;
		private BigDecimal paymentSumm;
		private String paymentPeriod;
		private String counterValue;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getFio() {
			return fio;
		}

		public void setFio(String fio) {
			this.fio = fio;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getServiceProviderName() {
			return serviceProviderName;
		}

		public void setServiceProviderName(String serviceProviderName) {
			this.serviceProviderName = serviceProviderName;
		}

		public BigDecimal getPaymentSumm() {
			return paymentSumm;
		}

		public void setPaymentSumm(BigDecimal paymentSumm) {
			this.paymentSumm = paymentSumm;
		}

		public String getPaymentPeriod() {
			return paymentPeriod;
		}

		public void setPaymentPeriod(String paymentPeriod) {
			this.paymentPeriod = paymentPeriod;
		}

		public String getCounterValue() {
			return counterValue;
		}

		public void setCounterValue(String counterValue) {
			this.counterValue = counterValue;
		}
	}
}
