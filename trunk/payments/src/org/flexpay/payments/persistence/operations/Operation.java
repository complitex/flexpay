package org.flexpay.payments.persistence.operations;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.persistence.Document;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class Operation extends DomainObject {

	private BigDecimal operationSumm;
	private BigDecimal operationInputSumm;
	private BigDecimal change;

	private Date creationDate;
	private String creatorUserName;

	private Date confirmationDate;
	private String confirmatorUserName;

	private Organization creatorOrganization;
	private Organization confirmatorOrganization;

//	private RegistryRecord registryRecord;

	private OperationLevel operationLevel;
	private OperationStatus operationStatus;

	private Set<Document> documents = Collections.emptySet();

	private Operation parentOperation;
	private Set<Operation> childOperations = Collections.emptySet();

	public BigDecimal getOperationSumm() {
		return operationSumm;
	}

	public void setOperationSumm(BigDecimal operationSumm) {
		this.operationSumm = operationSumm;
	}

	public BigDecimal getOperationInputSumm() {
		return operationInputSumm;
	}

	public void setOperationInputSumm(BigDecimal operationInputSumm) {
		this.operationInputSumm = operationInputSumm;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public String getConfirmatorUserName() {
		return confirmatorUserName;
	}

	public void setConfirmatorUserName(String confirmatorUserName) {
		this.confirmatorUserName = confirmatorUserName;
	}

	public Organization getCreatorOrganization() {
		return creatorOrganization;
	}

	public void setCreatorOrganization(@NotNull Organization creatorOrganization) {
		this.creatorOrganization = creatorOrganization;
	}

	public Organization getConfirmatorOrganization() {
		return confirmatorOrganization;
	}

	public void setConfirmatorOrganization(Organization confirmatorOrganization) {
		this.confirmatorOrganization = confirmatorOrganization;
	}

//	public RegistryRecord getRegistryRecord() {
//		return registryRecord;
//	}
//
//	public void setRegistryRecord(RegistryRecord registryRecord) {
//		this.registryRecord = registryRecord;
//	}

	public OperationLevel getOperationLevel() {
		return operationLevel;
	}

	public void setOperationLevel(OperationLevel operationLevel) {
		this.operationLevel = operationLevel;
	}

	public OperationStatus getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(OperationStatus operationStatus) {
		this.operationStatus = operationStatus;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public void addDocument(Document doc) {

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (documents == Collections.EMPTY_SET) {
			documents = CollectionUtils.set();
		}
		doc.setOperation(this);
		documents.add(doc);
	}

	public Operation getParentOperation() {
		return parentOperation;
	}

	public void setParentOperation(Operation parentOperation) {
		this.parentOperation = parentOperation;
	}

	public Set<Operation> getChildOperations() {
		return childOperations;
	}

	public void setChildOperations(Set<Operation> childOperations) {
		this.childOperations = childOperations;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Operation {").
				append("id", getId()).
				append("operationInputSumm", operationInputSumm).
				append("operationSumm", operationSumm).
				append("change", change).
				append("creationDate", creationDate).
				append("creatorUserName", creatorUserName).
				append("confirmationDate", confirmationDate).
				append("confirmatorUserName", confirmatorUserName).
				append("operationLevel", operationLevel).
				append("operationStatus", operationStatus).
				append("}").toString();
	}

}
