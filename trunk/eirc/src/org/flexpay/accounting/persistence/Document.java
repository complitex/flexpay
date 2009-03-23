package org.flexpay.accounting.persistence;

import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.common.persistence.DomainObject;

import java.math.BigDecimal;

public class Document extends DomainObject {

	private Operation operation;
	private DocumentType type;
	private BigDecimal summ;
	private EircAccount subjectDebet;
	private EircAccount subjectCredit;
	private DocumentStatus documentStatus;
	private int status;
	private RegistryRecord registryRecord;
	private Document referenceDocument;

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public BigDecimal getSumm() {
		return summ;
	}

	public void setSumm(BigDecimal summ) {
		this.summ = summ;
	}

	public EircAccount getSubjectDebet() {
		return subjectDebet;
	}

	public void setSubjectDebet(EircAccount subjectDebet) {
		this.subjectDebet = subjectDebet;
	}

	public EircAccount getSubjectCredit() {
		return subjectCredit;
	}

	public void setSubjectCredit(EircAccount subjectCredit) {
		this.subjectCredit = subjectCredit;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
}
