package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Set;
import java.util.Collections;

public class Document extends DomainObject {

	private BigDecimal summ;

	private Operation operation;

	private Organization creditorOrganization;
	private String creditorId;
	private Service service;

	private Organization debtorOrganization;
	private String debtorId;

	private RegistryRecord registryRecord;

	private Document referenceDocument;
	private DocumentType documentType;
	private DocumentStatus documentStatus;

	private String address;
	private String payerFIO;

	private Set<DocumentAddition> additions = Collections.emptySet();

	public BigDecimal getSumm() {
		return summ;
	}

	public void setSumm(BigDecimal summ) {
		this.summ = summ;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(@NotNull Operation operation) {
		this.operation = operation;
	}

	public RegistryRecord getRegistryRecord() {
		return registryRecord;
	}

	public void setRegistryRecord(RegistryRecord registryRecord) {
		this.registryRecord = registryRecord;
	}

	public Document getReferenceDocument() {
		return referenceDocument;
	}

	public void setReferenceDocument(Document referenceDocument) {
		this.referenceDocument = referenceDocument;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(@NotNull DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(@NotNull DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Organization getCreditorOrganization() {
		return creditorOrganization;
	}

	public void setCreditorOrganization(Organization creditorOrganization) {
		this.creditorOrganization = creditorOrganization;
	}

	public String getCreditorId() {
		return creditorId;
	}

	public void setCreditorId(String creditorId) {
		this.creditorId = creditorId;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Organization getDebtorOrganization() {
		return debtorOrganization;
	}

	public void setDebtorOrganization(Organization debtorOrganization) {
		this.debtorOrganization = debtorOrganization;
	}

	public String getDebtorId() {
		return debtorId;
	}

	public void setDebtorId(String debtorId) {
		this.debtorId = debtorId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPayerFIO() {
		return payerFIO;
	}

	public void setPayerFIO(String payerFIO) {
		this.payerFIO = payerFIO;
	}

	public Set<DocumentAddition> getAdditions() {
		return additions;
	}

	public void setAdditions(Set<DocumentAddition> additions) {
		this.additions = additions;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Document {").
				append("id", getId()).
				append("summ", summ).
				append("operation.id", operation.getId()).
				append("registryRecord", registryRecord).
				append("documentType", documentType).
				append("documentStatus", documentStatus).
				append("}").toString();
	}
}
