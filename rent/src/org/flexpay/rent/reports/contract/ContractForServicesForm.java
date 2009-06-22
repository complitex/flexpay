package org.flexpay.rent.reports.contract;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ContractForServicesForm implements Cloneable, Serializable {

	private String contractNumber;
	private String executor;
	private String headPosition;
	private String headFIO;
	private String renter;
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

	public Map<String, ?> getParams() {
		return map(ar("contractNumber",
				"executor",
				"headPosition",
				"headFIO",
				"renter",
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
				"endDate"
				),
				ar(contractNumber,
						executor,
						headPosition,
						headFIO,
						renter,
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
						endDate
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

	public String getHeadPosition() {
		return headPosition;
	}

	public void setHeadPosition(String headPosition) {
		this.headPosition = headPosition;
	}

	public String getHeadFIO() {
		return headFIO;
	}

	public void setHeadFIO(String headFIO) {
		this.headFIO = headFIO;
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String renter) {
		this.renter = renter;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("contractNumber", contractNumber).
				append("executor", executor).
				append("headPosition", headPosition).
				append("headFIO", headFIO).
				append("renter", renter).
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
