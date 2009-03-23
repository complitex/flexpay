package org.flexpay.accounting.persistence;

import org.flexpay.eirc.persistence.Organization;
import org.flexpay.common.persistence.DomainObject;

import java.math.BigDecimal;
import java.util.Date;

public class Operation extends DomainObject {

	private Long id;
	private Long operationCode; //(??)
	private BigDecimal operationSumm;
	private BigDecimal operationInputSumm;
	private BigDecimal change;

	private Organization creatorOrganization;
	private String creatorUserName;
	private Date creationDate;

	private Organization confirmatorOrganization;
	private String confirmatorUserName;
	private Date confirmationDate;

	private OperationStatus operationStatus;
	private OperationLevel level;

	private Operation operationReference;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(Long operationCode) {
		this.operationCode = operationCode;
	}

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

	public Organization getCreatorOrganization() {
		return creatorOrganization;
	}

	public void setCreatorOrganization(Organization creatorOrganization) {
		this.creatorOrganization = creatorOrganization;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Organization getConfirmatorOrganization() {
		return confirmatorOrganization;
	}

	public void setConfirmatorOrganization(Organization confirmatorOrganization) {
		this.confirmatorOrganization = confirmatorOrganization;
	}

	public String getConfirmatorUserName() {
		return confirmatorUserName;
	}

	public void setConfirmatorUserName(String confirmatorUserName) {
		this.confirmatorUserName = confirmatorUserName;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public OperationStatus getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(OperationStatus operationStatus) {
		this.operationStatus = operationStatus;
	}

	public OperationLevel getLevel() {
		return level;
	}

	public void setLevel(OperationLevel level) {
		this.level = level;
	}

	public Operation getOperationReference() {
		return operationReference;
	}

	public void setOperationReference(Operation operationReference) {
		this.operationReference = operationReference;
	}
}
