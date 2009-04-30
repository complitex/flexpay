package org.flexpay.payments.actions.operations;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.OperationStatusService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class OperationsListAction extends FPActionWithPagerSupport<Operation> {

	// form data
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private String status;
	private Long selectedOperationId;

	// submit flags
	private String dateSubmitted;
	private String registerSubmitted;
	private String returnSubmitted;
	private String deleteSubmitted;

	private List<Operation> operations = CollectionUtils.list();

	// required services
	private OperationService operationService;
	private OperationStatusService operationStatusService;

	@NotNull
	protected String doExecute() throws Exception {

		if (isDateSubmitted()) { // operations filtering by date submitted
			if (doValidate()) {
				loadOperations();
			}
			return SUCCESS;
		}

		if (isStatusSubmitted()) { // if status change submitted
			if (doValidate()) {
				updateOperationStatus();				
				loadOperations();
			}
			return SUCCESS;
		}

		initFiltersWithDefaults();
		loadOperations();
		return SUCCESS;
	}

	private void updateOperationStatus() throws FlexPayException {
		OperationStatus operationStatus = operationStatusService.read(Integer.parseInt(status));
		Operation operation = operationService.read(new Stub<Operation>(selectedOperationId));
		operation.setOperationStatus(operationStatus);
		operationService.save(operation);
	}

	private void loadOperations() {
		Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
		Date endDate = DateUtil.truncateDay(endDateFilter.getDate());
		endDate = DateUtils.addDays(endDate, 1);

		operations = operationService.listPaymentOperations(beginDate, endDate, getPager());
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

	private boolean isDateSubmitted() {
		return dateSubmitted != null;
	}

	private boolean isStatusSubmitted() {
		return registerSubmitted != null || returnSubmitted != null || deleteSubmitted != null;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSelectedOperationId() {
		return selectedOperationId;
	}

	public void setSelectedOperationId(Long selectedOperationId) {
		this.selectedOperationId = selectedOperationId;
	}

	public void setDateSubmitted(String dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	public void setRegisterSubmitted(String registerSubmitted) {
		this.registerSubmitted = registerSubmitted;
	}

	public void setReturnSubmitted(String returnSubmitted) {
		this.returnSubmitted = returnSubmitted;
	}

	public void setDeleteSubmitted(String deleteSubmitted) {
		this.deleteSubmitted = deleteSubmitted;
	}

	// required services
	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setOperationStatusService(OperationStatusService operationStatusService) {
		this.operationStatusService = operationStatusService;
	}
}
