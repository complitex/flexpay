package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Response for quittance details request from external system
 */
public class QuittanceDetailsResponse implements Serializable {

	public static final int CODE_SUCCESS = 1;
	public static final int CODE_ERROR_QUITTANCE_NOT_FOUND = 10;
	public static final int CODE_ERROR_ACCOUNT_NOT_FOUND = 11;
	public static final int CODE_ERROR_APARTMENT_NOT_FOUND = 12;
	public static final int CODE_ERROR_INVALID_QUITTANCE_NUMBER = 13;

	/**
	 * Response error code
	 */
	private int errorCode;
	private String requestId;

	private QuittanceInfo[] infos;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public QuittanceInfo[] getInfos() {
		return infos;
	}

	public void setInfos(QuittanceInfo[] infos) {
		this.infos = infos;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("errorCode", errorCode).
				append("requestId", requestId).
				append("infos", infos).
				toString();
	}

	public static class QuittanceInfo implements Serializable {

		private Date creationDate;
		private String serviceOrganizationMasterIndex;
		private Integer orderNumber;
		private Date dateFrom;
		private Date dateTill;
		private String accountNumber;
		private String personFirstName;
		private String personMiddleName;
		private String personLastName;
		private String apartmentMasterIndex;
		private String country;
		private String region;
		private String town;
		private String street;
		private String building;
		private String apartmentNumber;

		private BigDecimal totalPayed;
		private BigDecimal totalToPay;
		private ServiceDetails[] detailses;

		public Date getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		public String getServiceOrganizationMasterIndex() {
			return serviceOrganizationMasterIndex;
		}

		public void setServiceOrganizationMasterIndex(String serviceOrganizationMasterIndex) {
			this.serviceOrganizationMasterIndex = serviceOrganizationMasterIndex;
		}

		public Integer getOrderNumber() {
			return orderNumber;
		}

		public void setOrderNumber(Integer orderNumber) {
			this.orderNumber = orderNumber;
		}

		public Date getDateFrom() {
			return dateFrom;
		}

		public void setDateFrom(Date dateFrom) {
			this.dateFrom = dateFrom;
		}

		public Date getDateTill() {
			return dateTill;
		}

		public void setDateTill(Date dateTill) {
			this.dateTill = dateTill;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getPersonFirstName() {
			return personFirstName;
		}

		public void setPersonFirstName(String personFirstName) {
			this.personFirstName = personFirstName;
		}

		public String getPersonMiddleName() {
			return personMiddleName;
		}

		public void setPersonMiddleName(String personMiddleName) {
			this.personMiddleName = personMiddleName;
		}

		public String getPersonLastName() {
			return personLastName;
		}

		public void setPersonLastName(String personLastName) {
			this.personLastName = personLastName;
		}

		public String getApartmentMasterIndex() {
			return apartmentMasterIndex;
		}

		public void setApartmentMasterIndex(String apartmentMasterIndex) {
			this.apartmentMasterIndex = apartmentMasterIndex;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getTown() {
			return town;
		}

		public void setTown(String town) {
			this.town = town;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getBuilding() {
			return building;
		}

		public void setBuilding(String building) {
			this.building = building;
		}

		public String getApartmentNumber() {
			return apartmentNumber;
		}

		public void setApartmentNumber(String apartmentNumber) {
			this.apartmentNumber = apartmentNumber;
		}

		public ServiceDetails[] getDetailses() {
			return detailses;
		}

		public void setDetailses(ServiceDetails[] detailses) {
			this.detailses = detailses;
		}

		public BigDecimal getTotalPayed() {
			return totalPayed;
		}

		public void setTotalPayed(BigDecimal totalPayed) {
			this.totalPayed = totalPayed;
		}

		public BigDecimal getTotalToPay() {
			return totalToPay;
		}

		public void setTotalToPay(BigDecimal totalToPay) {
			this.totalToPay = totalToPay;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).
					append("creationDate", creationDate).
					append("serviceOrganizationMasterIndex", serviceOrganizationMasterIndex).
					append("orderNumber", orderNumber).
					append("dateFrom", dateFrom).
					append("dateTill", dateTill).
					append("accountNumber", accountNumber).
					append("personFirstName", personFirstName).
					append("personMiddleName", personMiddleName).
					append("personLastName", personLastName).
					append("apartmentMasterIndex", apartmentMasterIndex).
					append("country", country).
					append("region", region).
					append("town", town).
					append("street", street).
					append("building", building).
					append("apartmentNumber", apartmentNumber).
					append("totalPayed", totalPayed).
					append("totalToPay", totalToPay).
					append("detailses", detailses).
					toString();
		}

		public static class ServiceDetails implements Serializable {

			private BigDecimal incomingBalance;
			private BigDecimal outgoingBalance;
			private BigDecimal amount;
			private BigDecimal expence;
			private BigDecimal rate;
			private BigDecimal recalculation;
			private BigDecimal benifit;
			private BigDecimal subsidy;
			private BigDecimal payment;
			private BigDecimal payed;
			private String serviceMasterIndex;

			public BigDecimal getIncomingBalance() {
				return incomingBalance;
			}

			public void setIncomingBalance(BigDecimal incomingBalance) {
				this.incomingBalance = incomingBalance;
			}

			public BigDecimal getOutgoingBalance() {
				return outgoingBalance;
			}

			public void setOutgoingBalance(BigDecimal outgoingBalance) {
				this.outgoingBalance = outgoingBalance;
			}

			public BigDecimal getAmount() {
				return amount;
			}

			public void setAmount(BigDecimal amount) {
				this.amount = amount;
			}

			public BigDecimal getExpence() {
				return expence;
			}

			public void setExpence(BigDecimal expence) {
				this.expence = expence;
			}

			public BigDecimal getRate() {
				return rate;
			}

			public void setRate(BigDecimal rate) {
				this.rate = rate;
			}

			public BigDecimal getRecalculation() {
				return recalculation;
			}

			public void setRecalculation(BigDecimal recalculation) {
				this.recalculation = recalculation;
			}

			public BigDecimal getBenifit() {
				return benifit;
			}

			public void setBenifit(BigDecimal benifit) {
				this.benifit = benifit;
			}

			public BigDecimal getSubsidy() {
				return subsidy;
			}

			public void setSubsidy(BigDecimal subsidy) {
				this.subsidy = subsidy;
			}

			public BigDecimal getPayment() {
				return payment;
			}

			public void setPayment(BigDecimal payment) {
				this.payment = payment;
			}

			public BigDecimal getPayed() {
				return payed;
			}

			public void setPayed(BigDecimal payed) {
				this.payed = payed;
			}

			public String getServiceMasterIndex() {
				return serviceMasterIndex;
			}

			public void setServiceMasterIndex(String serviceMasterIndex) {
				this.serviceMasterIndex = serviceMasterIndex;
			}

			@Override
			public String toString() {
				return new ToStringBuilder(this).
						append("incomingBalance", incomingBalance).
						append("outgoingBalance", outgoingBalance).
						append("amount", amount).
						append("expence", expence).
						append("rate", rate).
						append("recalculation", recalculation).
						append("benifit", benifit).
						append("subsidy", subsidy).
						append("payment", payment).
						append("payed", payed).
						append("serviceMasterIndex", serviceMasterIndex).
						toString();
			}
		}
	}
}
