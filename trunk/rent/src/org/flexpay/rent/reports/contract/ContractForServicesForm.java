package org.flexpay.rent.reports.contract;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;

public class ContractForServicesForm implements Cloneable, Serializable {

	private String contractNumber;
	private String executor;
	private String executorAddress;
	private String executorBankDetails;
	private String executorChiefFIO;
	private String executorChiefPosition;
	private String renter;
	private String renterJuridicalAddress;
	private String renterBankDetails;
	private String renterChiefFIO;
	private String registrationDocument;
	private Date contractDate;

	private BigDecimal totalSquare = BigDecimal.ZERO;
	private String address;
	private String document;
	private String businessType;
	private String serviceProviders;
	private String services;
	private String documents;
	private Date beginDate;
	private Date endDate;

	private String centralHeatingSP;
	private String centralHeatingSPAccount;
	private BigDecimal centralHeatingHeatedSquare = BigDecimal.ZERO;
	private Integer centralHeatingDesignLoad;

	private String hotWaterSP;
	private String hotWaterSPAccount;
	private Integer hotWaterPercent;
	private Integer hotWaterDesignLoad;

	private String coldWaterSP;
	private String coldWaterSPAccount;
	private Integer coldWaterPercent;
	private Integer coldWaterSize;

	private String waterHeaterSP;
	private String waterHeaterSPAccount;
	private Integer waterHeaterPercent;
	private Integer waterHeaterSize;

	public Map<String, ?> getParams() {
		return map(ar("contractNumber",
				"executor",
				"executorAddress",
				"executorBankDetails",
				"executorChiefFIO",
				"executorChiefPosition",
				"renter",
				"renterJuridicalAddress",
				"renterBankDetails",
				"renterChiefFIO",
				"registrationDocument",
				"contractDate",
				"totalSquare",
				"address",
				"document",
				"businessType",
				"serviceProviders",
				"services",
				"documents",
				"beginDate",
				"endDate",
				"centralHeatingSP",
				"centralHeatingSPAccount",
				"centralHeatingHeatedSquare",
				"centralHeatingDesignLoad",
				"hotWaterSP",
				"hotWaterSPAccount",
				"hotWaterPercent",
				"hotWaterDesignLoad",
				"coldWaterSP",
				"coldWaterSPAccount",
				"coldWaterPercent",
				"coldWaterSize",
				"waterHeaterSP",
				"waterHeaterSPAccount",
				"waterHeaterPercent",
				"waterHeaterSize"
				),
				ar(contractNumber,
						executor,
						executorAddress,
						executorBankDetails,
						executorChiefFIO,
						executorChiefPosition,
						renter,
						renterJuridicalAddress,
						renterBankDetails,
						renterChiefFIO,
						registrationDocument,
						contractDate,
						totalSquare,
						address,
						document,
						businessType,
						serviceProviders,
						services,
						documents,
						beginDate,
						endDate,
						centralHeatingSP,
						centralHeatingSPAccount,
						centralHeatingHeatedSquare,
						centralHeatingDesignLoad,
						hotWaterSP,
						hotWaterSPAccount,
						hotWaterPercent,
						hotWaterDesignLoad,
						coldWaterSP,
						coldWaterSPAccount,
						coldWaterPercent,
						coldWaterSize,
						waterHeaterSP,
						waterHeaterSPAccount,
						waterHeaterPercent,
						waterHeaterSize
				));
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getExecutorAddress() {
		return executorAddress;
	}

	public void setExecutorAddress(String executorAddress) {
		this.executorAddress = executorAddress;
	}

	public String getExecutorBankDetails() {
		return executorBankDetails;
	}

	public void setExecutorBankDetails(String executorBankDetails) {
		this.executorBankDetails = executorBankDetails;
	}

	public String getExecutorChiefFIO() {
		return executorChiefFIO;
	}

	public void setExecutorChiefFIO(String executorChiefFIO) {
		this.executorChiefFIO = executorChiefFIO;
	}

	public String getExecutorChiefPosition() {
		return executorChiefPosition;
	}

	public void setExecutorChiefPosition(String executorChiefPosition) {
		this.executorChiefPosition = executorChiefPosition;
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String renter) {
		this.renter = renter;
	}

	public String getRenterJuridicalAddress() {
		return renterJuridicalAddress;
	}

	public void setRenterJuridicalAddress(String renterJuridicalAddress) {
		this.renterJuridicalAddress = renterJuridicalAddress;
	}

	public String getRenterBankDetails() {
		return renterBankDetails;
	}

	public void setRenterBankDetails(String renterBankDetails) {
		this.renterBankDetails = renterBankDetails;
	}

	public String getRenterChiefFIO() {
		return renterChiefFIO;
	}

	public void setRenterChiefFIO(String renterChiefFIO) {
		this.renterChiefFIO = renterChiefFIO;
	}

	public String getRegistrationDocument() {
		return registrationDocument;
	}

	public void setRegistrationDocument(String registrationDocument) {
		this.registrationDocument = registrationDocument;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public BigDecimal getTotalSquare() {
		return totalSquare;
	}

	public void setTotalSquare(BigDecimal totalSquare) {
		this.totalSquare = totalSquare;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getServiceProviders() {
		return serviceProviders;
	}

	public void setServiceProviders(String serviceProviders) {
		this.serviceProviders = serviceProviders;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
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

	public String getCentralHeatingSP() {
		return centralHeatingSP;
	}

	public void setCentralHeatingSP(String centralHeatingSP) {
		this.centralHeatingSP = centralHeatingSP;
	}

	public String getCentralHeatingSPAccount() {
		return centralHeatingSPAccount;
	}

	public void setCentralHeatingSPAccount(String centralHeatingSPAccount) {
		this.centralHeatingSPAccount = centralHeatingSPAccount;
	}

	public BigDecimal getCentralHeatingHeatedSquare() {
		return centralHeatingHeatedSquare;
	}

	public void setCentralHeatingHeatedSquare(BigDecimal centralHeatingHeatedSquare) {
		this.centralHeatingHeatedSquare = centralHeatingHeatedSquare;
	}

	public Integer getCentralHeatingDesignLoad() {
		return centralHeatingDesignLoad;
	}

	public void setCentralHeatingDesignLoad(Integer centralHeatingDesignLoad) {
		this.centralHeatingDesignLoad = centralHeatingDesignLoad;
	}

	public String getHotWaterSP() {
		return hotWaterSP;
	}

	public void setHotWaterSP(String hotWaterSP) {
		this.hotWaterSP = hotWaterSP;
	}

	public String getHotWaterSPAccount() {
		return hotWaterSPAccount;
	}

	public void setHotWaterSPAccount(String hotWaterSPAccount) {
		this.hotWaterSPAccount = hotWaterSPAccount;
	}

	public Integer getHotWaterPercent() {
		return hotWaterPercent;
	}

	public void setHotWaterPercent(Integer hotWaterPercent) {
		this.hotWaterPercent = hotWaterPercent;
	}

	public Integer getHotWaterDesignLoad() {
		return hotWaterDesignLoad;
	}

	public void setHotWaterDesignLoad(Integer hotWaterDesignLoad) {
		this.hotWaterDesignLoad = hotWaterDesignLoad;
	}

	public String getColdWaterSP() {
		return coldWaterSP;
	}

	public void setColdWaterSP(String coldWaterSP) {
		this.coldWaterSP = coldWaterSP;
	}

	public String getColdWaterSPAccount() {
		return coldWaterSPAccount;
	}

	public void setColdWaterSPAccount(String coldWaterSPAccount) {
		this.coldWaterSPAccount = coldWaterSPAccount;
	}

	public Integer getColdWaterPercent() {
		return coldWaterPercent;
	}

	public void setColdWaterPercent(Integer coldWaterPercent) {
		this.coldWaterPercent = coldWaterPercent;
	}

	public Integer getColdWaterSize() {
		return coldWaterSize;
	}

	public void setColdWaterSize(Integer coldWaterSize) {
		this.coldWaterSize = coldWaterSize;
	}

	public String getWaterHeaterSP() {
		return waterHeaterSP;
	}

	public void setWaterHeaterSP(String waterHeaterSP) {
		this.waterHeaterSP = waterHeaterSP;
	}

	public String getWaterHeaterSPAccount() {
		return waterHeaterSPAccount;
	}

	public void setWaterHeaterSPAccount(String waterHeaterSPAccount) {
		this.waterHeaterSPAccount = waterHeaterSPAccount;
	}

	public Integer getWaterHeaterPercent() {
		return waterHeaterPercent;
	}

	public void setWaterHeaterPercent(Integer waterHeaterPercent) {
		this.waterHeaterPercent = waterHeaterPercent;
	}

	public Integer getWaterHeaterSize() {
		return waterHeaterSize;
	}

	public void setWaterHeaterSize(Integer waterHeaterSize) {
		this.waterHeaterSize = waterHeaterSize;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("contractNumber", contractNumber).
				append("executor", executor).
				append("executorAddress", executorAddress).
				append("executorBankDetails", executorBankDetails).
				append("executorChiefFIO", executorChiefFIO).
				append("executorChiefPosition", executorChiefPosition).
				append("renter", renter).
				append("renterJuridicalAddress", renterJuridicalAddress).
				append("renterBankDetails", renterBankDetails).
				append("renterChiefFIO", renterChiefFIO).
				append("registrationDocument", registrationDocument).
				append("contractDate", contractDate).
				append("totalSquare", totalSquare).
				append("address", address).
				append("document", document).
				append("businessType", businessType).
				append("serviceProviders", serviceProviders).
				append("services", services).
				append("documents", documents).
				append("beginDate", beginDate).
				append("endDate", endDate).
				toString();
	}

}
