package org.flexpay.payments.reports.payments;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AccPaymentReportData {

	private Date beginDate;
	private Date endDate;
	private String paymentCollectorOrgName;
	private String paymentCollectorOrgAddress;
	private String accountantFio;
	private Date creationDate;

	private List<PaymentDetails> detailses;

	public static class PaymentDetails {

		private Long objectId;
		private String divisionName;
		private String divisionAddress;
		private BigDecimal paymentKvartplata;
		private BigDecimal paymentDogs;
		private BigDecimal paymentGarage;
		private BigDecimal paymentWarming;
		private BigDecimal paymentHotWater;
		private BigDecimal paymentColdWater;
		private BigDecimal paymentSewer;
		private BigDecimal sum;

		private List<PaymentDetails> childDetailses;

		public Long getObjectId() {
			return objectId;
		}

		public void setObjectId(Long objectId) {
			this.objectId = objectId;
		}

		public String getDivisionName() {
			return divisionName;
		}

		public void setDivisionName(String divisionName) {
			this.divisionName = divisionName;
		}

		public String getDivisionAddress() {
			return divisionAddress;
		}

		public void setDivisionAddress(String divisionAddress) {
			this.divisionAddress = divisionAddress;
		}

		public BigDecimal getPaymentKvartplata() {
			return paymentKvartplata;
		}

		public void setPaymentKvartplata(BigDecimal paymentKvartplata) {
			this.paymentKvartplata = paymentKvartplata;
		}

		public BigDecimal getPaymentDogs() {
			return paymentDogs;
		}

		public void setPaymentDogs(BigDecimal paymentDogs) {
			this.paymentDogs = paymentDogs;
		}

		public BigDecimal getPaymentGarage() {
			return paymentGarage;
		}

		public void setPaymentGarage(BigDecimal paymentGarage) {
			this.paymentGarage = paymentGarage;
		}

		public BigDecimal getPaymentWarming() {
			return paymentWarming;
		}

		public void setPaymentWarming(BigDecimal paymentWarming) {
			this.paymentWarming = paymentWarming;
		}

		public BigDecimal getPaymentHotWater() {
			return paymentHotWater;
		}

		public void setPaymentHotWater(BigDecimal paymentHotWater) {
			this.paymentHotWater = paymentHotWater;
		}

		public BigDecimal getPaymentColdWater() {
			return paymentColdWater;
		}

		public void setPaymentColdWater(BigDecimal paymentColdWater) {
			this.paymentColdWater = paymentColdWater;
		}

		public BigDecimal getPaymentSewer() {
			return paymentSewer;
		}

		public void setPaymentSewer(BigDecimal paymentSewer) {
			this.paymentSewer = paymentSewer;
		}

		public BigDecimal getSum() {
			return sum;
		}

		public void setSum(BigDecimal sum) {
			this.sum = sum;
		}

		public List<PaymentDetails> getChildDetailses() {
			return childDetailses;
		}

		public void setChildDetailses(List<PaymentDetails> childDetailses) {
			this.childDetailses = childDetailses;
		}
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

	public List<PaymentDetails> getDetailses() {
		return detailses;
	}

	public void setDetailses(List<PaymentDetails> detailses) {
		this.detailses = detailses;
	}

	public String getPaymentCollectorOrgName() {
		return paymentCollectorOrgName;
	}

	public void setPaymentCollectorOrgName(String paymentCollectorOrgName) {
		this.paymentCollectorOrgName = paymentCollectorOrgName;
	}
}
