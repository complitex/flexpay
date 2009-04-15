package org.flexpay.payments.actions.operations;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.DocumentStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;

public class OperationsListAction extends FPActionWithPagerSupport<Operation> {

	// form data
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();

	private List<Operation> operations = CollectionUtils.list();

	// required services
	private OperationService operationService;

	@NotNull
	protected String doExecute() throws Exception {

		if (isSubmit()) {
			if (!doValidate()) {
				return SUCCESS;
			}
		} else {
			initFiltersWithDefaults();
		}
		
		Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
		Date endDate = DateUtil.truncateDay(endDateFilter.getDate());
		endDate = DateUtils.addDays(endDate, 1);

		operations = operationService.listPaymentOperations(beginDate, endDate, getPager());

		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}

	private boolean doValidate() {

		Date beginDate = beginDateFilter.getDate();
		Date endDate = endDateFilter.getDate();

		if (beginDate.after(endDate)) {
			addActionError(getText("payments.error.operations.list.begin_date_must_be_before_end_date"));
		}

		return !hasActionErrors();
	}

	private void initFiltersWithDefaults() {
		beginDateFilter.setDate(DateUtil.now());
		endDateFilter.setDate(DateUtil.now());
	}

	// rendering utility methods
	public boolean operationsListIsEmpty() {
		return operations.isEmpty();
	}

	public int getTotalOperations() {
		return getPager().getTotalNumberOfElements();
	}

	public boolean isOperationCreated(int status) {
		return status == OperationStatus.CREATED;
	}

	public boolean isOperationRegistered(int status) {
		return status == OperationStatus.REGISTERED;		
	}

	public boolean isOperationReturned(int status) {
		return status == OperationStatus.RETURNED;
	}

	public boolean isOperationError(int status) {
		return status == OperationStatus.ERROR;
	}

	public boolean isDocumentCreated(int status) {
		return status == DocumentStatus.CREATED;
	}

	public boolean isDocumentRegistered(int status) {
		return status == DocumentStatus.REGISTERED;
	}

	public boolean isDocumentReturned(int status) {
		return status == DocumentStatus.RETURNED;
	}

	public boolean isDocumentError(int status) {
		return status == DocumentStatus.ERROR;
	}

	public boolean isDocumentDeleted(int status) {
		return status == DocumentStatus.DELETED;
	}

	// form data
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	// required services
	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}
}
