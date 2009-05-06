package org.flexpay.payments.actions.operations;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class OperationsListAction extends FPActionWithPagerSupport<Operation> {

	// form data
	private List<ServiceType> serviceTypes = CollectionUtils.list();

	// date filters
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();

	// time boundaries
	private String beginTime;
	private String endTime;

	// summ boundaries
	private String minimalSumm;
	private String maximalSumm;

	// selected service type id
	private Long serviceTypeId;

	// submit flags
	private String filterSubmitted;
	private String registerSubmitted;
	private String returnSubmitted;
	private String deleteSubmitted;

	// document search flag
	private String documentSearchEnabled;

	// status provider operations
	private String status;
	private Long selectedOperationId;

	// search results data
	private List<Operation> operations = CollectionUtils.list();
	private List<Long> highlightedDocumentIds = CollectionUtils.list();

	// required services
	private OperationService operationService;
	private OperationStatusService operationStatusService;
	private DocumentStatusService documentStatusService;
	private DocumentService documentService;
	private ServiceTypeService serviceTypeService;

	@NotNull
	protected String doExecute() throws Exception {

		//loadServiceTypes();

		if (dateFiltersAreEmpty()) {
			initFiltersWithDefaults();
		}

		if (isFilterSubmitted()) { // operations filtering by date submitted
			if (doFilterValidation()) {
				loadOperations();
			}
			return SUCCESS;
		} else if (isStatusSubmitted()) { // if status change submitted
			updateOperationStatus();
			loadOperations();
			return SUCCESS;
		} else { // nothing is submitted
			loadOperations();
			return SUCCESS;
		}
	}

	private void loadOperations() {

		// FIXME old code placed here to make working demo MUST BE ELIMINATED
		Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
		Date endDate = DateUtil.truncateDay(endDateFilter.getDate());
		endDate = DateUtils.addDays(endDate, 1);
		operations = operationService.listPaymentOperations(beginDate, endDate, getPager());
		// FIXME end of old code

		/*
		BigDecimal minSumm = new BigDecimal(minimalSumm);
		BigDecimal maxSumm = new BigDecimal(maximalSumm);

		// service type id is field named serviceTypeId

		if (documentSearchEnabled()) {

			// TODO refactor
			Date beginDate = DateUtil.truncateDay(beginDateFilter.getDate());
			Date endDate = DateUtil.truncateDay(endDateFilter.getDate());
			endDate = DateUtils.setHours(endDate, 23);
			endDate = DateUtils.setMinutes(endDate, 59);
			endDate = DateUtils.setSeconds(endDate, 59);

			//List<Operation> searchResults = operationService.listPaymentsOperations(serviceTypeId, beginDate, endDate, minSumm, maxSumm, getPager())

			// TODO save highlighted document identifiers here

			// TODO read full operations data and putting them into operations
		} else {
			//Date beginDate = DateUtil.getFullDate(beginDateFilter.getDate(), beginTime);
			//Date endDate = DateUtil.getFullDate(endDateFilter.getDate(), endTime);

			// TODO implement
			//operations = operationService.listPaymentOperations(beginDate, endDate, minSumm, maxSumm, getPager());
		}
		*/
	}

	private void updateOperationStatus() throws FlexPayException {
		OperationStatus operationStatus = operationStatusService.read(Integer.parseInt(status));
		Operation operation = operationService.read(new Stub<Operation>(selectedOperationId));
		operation.setOperationStatus(operationStatus);

		// setting documents status
		for (Document document : operation.getDocuments()) {
			DocumentStatus documentStatus = documentStatusService.read(Integer.parseInt(status));
			document.setDocumentStatus(documentStatus);
			documentService.save(document);
		}

		operationService.save(operation);
	}

	private void loadServiceTypes() {
		serviceTypes = serviceTypeService.listAllServiceTypes();
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}

	private boolean doFilterValidation() {

		// dates validation
		Date beginDate = beginDateFilter.getDate();
		Date endDate = endDateFilter.getDate();

		if (beginDate.after(endDate)) {
			addActionError(getText("payments.error.operations.list.begin_date_must_be_before_end_date"));
		}

		// time validation
		// TODO implement

		// summs validation
		// TODO implement

		return !hasActionErrors();
	}

	private void initFiltersWithDefaults() {
		beginDateFilter.setDate(DateUtil.now());
		endDateFilter.setDate(DateUtil.now());
	}

	private boolean dateFiltersAreEmpty() {
		return beginDateFilter.dateIsEmpty() && endDateFilter.dateIsEmpty();
	}

	private boolean documentSearchEnabled() {
		return StringUtils.isNotEmpty(documentSearchEnabled);
	}

	private boolean isFilterSubmitted() {
		return filterSubmitted != null;
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public String getMinimalSumm() {
		return minimalSumm;
	}

	public void setMinimalSumm(String minimalSumm) {
		this.minimalSumm = minimalSumm;
	}

	public String getMaximalSumm() {
		return maximalSumm;
	}

	public void setMaximalSumm(String maximalSumm) {
		this.maximalSumm = maximalSumm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Long getSelectedOperationId() {
		return selectedOperationId;
	}

	public void setSelectedOperationId(Long selectedOperationId) {
		this.selectedOperationId = selectedOperationId;
	}

	public void setFilterSubmitted(String filterSubmitted) {
		this.filterSubmitted = filterSubmitted;
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

	public String getDocumentSearchEnabled() {
		return documentSearchEnabled;
	}

	public void setDocumentSearchEnabled(String documentSearchEnabled) {
		this.documentSearchEnabled = documentSearchEnabled;
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

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Required
	public void setDocumentStatusService(DocumentStatusService documentStatusService) {
		this.documentStatusService = documentStatusService;
	}

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
}
