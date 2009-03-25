package org.flexpay.accounting.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.accounting.persistence.operations.Operation;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.RegistryRecord;

import java.math.BigDecimal;

public class Document extends DomainObject {

	private BigDecimal summ;
	private Integer status;

	private Operation operation;

	private EircSubject subjectDebet;
	private EircSubject subjectCredit;
	private RegistryRecord registryRecord;

	private Document referenceDocument;
	private DocumentType documentType;
	private DocumentStatus documentStatus;

	public BigDecimal getSumm() {
		return summ;
	}

	public void setSumm(BigDecimal summ) {
		this.summ = summ;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public EircSubject getSubjectDebet() {
		return subjectDebet;
	}

	public void setSubjectDebet(EircSubject subjectDebet) {
		this.subjectDebet = subjectDebet;
	}

	public EircSubject getSubjectCredit() {
		return subjectCredit;
	}

	public void setSubjectCredit(EircSubject subjectCredit) {
		this.subjectCredit = subjectCredit;
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

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Document {").
				append("summ", summ).
				append("status", status).
				append("operation.id", operation.getId()).
				append("subjectDebet.id", subjectDebet.getId()).
				append("subjectCredit.id", subjectCredit.getId()).
				append("registryRecord", registryRecord).
				append("documentType", documentType).
				append("documentStatus", documentStatus).
				append("}").toString();
	}

}
