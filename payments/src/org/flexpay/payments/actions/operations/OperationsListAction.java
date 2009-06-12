package org.flexpay.payments.actions.operations;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.process.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.*;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.jbpm.graph.def.Transition;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.JbpmContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class OperationsListAction extends CashboxCookieWithPagerActionSupport<Operation> {
	private static final String PROCESS_STATUS = "PROCESS_STATUS";
	// form data
	private List<ServiceType> serviceTypes = CollectionUtils.list();

	// selected service type id
	private Long serviceTypeId;

	// date/time filters
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();

	// summ filter limits
	private BigDecimal minimalSumm;
	private BigDecimal maximalSumm;

	// submit flags
	private String filterSubmitted;
	private String registerSubmitted;
	private String returnSubmitted;
	private String deleteSubmitted;

	// document search flag
	private String documentSearchEnabled = "false";

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
	private OrganizationService organizationService;
	private CashboxService cashboxService;
	private ProcessManager processManager;

	private List<String> processButtons;
	private String processStatus;
	private String activity;
	private Long taskInstanceId;

	@NotNull
	protected String doExecute() throws Exception {

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		PaymentPoint paymentPoint = cashbox.getPaymentPoint();
		//signal if taskInstanceId and activity not null
		processButtons = Collections.emptyList();
		Long processInstanceId = paymentPoint.getTradingDayProcessInstanceId();

		log.debug("processInstanceId = {}", processInstanceId);

		if (processInstanceId != null && processInstanceId !=0) {
			if (activity != null && activity.length() > 0 && taskInstanceId != null) {
				processManager.execute(new ContextCallback<Void>() {
					public Void doInContext(@NotNull JbpmContext context) {
						TaskInstance taskInstance = context.getTaskMgmtSession().getTaskInstance(taskInstanceId);
						if (taskInstance.isSignalling()) {
							taskInstance.getProcessInstance().signal(activity);
						}
						return null;
					}
				});
			}
			//fetch taskInstance  and transitions
			final TaskInstance taskInstance = TaskHelper.getTaskInstance(processManager, processInstanceId, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR, log);


			if (taskInstance != null) {
				taskInstanceId = taskInstance.getId();
				processButtons = processManager.execute(new ContextCallback<List<String>>() {
					public List<String> doInContext(@NotNull JbpmContext context) {
						TaskInstance currentTaskInstance = context.getTaskInstance(taskInstance.getId());
						List<String> currentTarnsitionNameList = new ArrayList<String>();
						if (currentTaskInstance != null) {
							for (Object o : currentTaskInstance.getProcessInstance().getRootToken().getAvailableTransitions()) {
								Transition t = (Transition) o;
								currentTarnsitionNameList.add(t.getName());
							}
						}
						return currentTarnsitionNameList;
					}
				});
			}
			
			processStatus = (String)processManager.getProcessInstanceInfo(processInstanceId).getParameters().get(PROCESS_STATUS);
		}
		loadServiceTypes();

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

	// load operations according to search criteria
	private void loadOperations() {

		if (documentSearchEnabled()) {
			Date begin = beginDateFilter.getDate();
			Date end = endDateFilter.getDate();
			end = DateUtils.setHours(end, 23);
			end = DateUtils.setMinutes(end, 59);
			end = DateUtils.setSeconds(end, 59);

			List<Operation> searchResults = operationService.searchDocuments(getSelfOrganization(), serviceTypeId, begin, end, minimalSumm, maximalSumm, getPager());
			loadFullOperationsData(searchResults);
			highlightedDocumentIds = getHighlightedDocumentIds(searchResults);
		} else {
			Date begin = beginTimeFilter.setTime(beginDateFilter.getDate());
			Date end = endTimeFilter.setTime(endDateFilter.getDate());
			List<Operation> searchResults = operationService.searchOperations(getSelfOrganization(), begin, end, minimalSumm, maximalSumm, getPager());
			loadFullOperationsData(searchResults);
		}
	}

	private void loadFullOperationsData(List<Operation> searchResults) {
		for (Operation operation : searchResults) {
			operations.add(operationService.read(new Stub<Operation>(operation.getId())));
		}
	}

	private List<Long> getHighlightedDocumentIds(List<Operation> operations) {

		List<Long> result = CollectionUtils.list();
		for (Operation operation : operations) {
			List<Document> docs = documentService.searchDocuments(operation, serviceTypeId, minimalSumm, maximalSumm);
			for (Document doc : docs) {
				result.add(doc.getId());
			}
		}
		return result;
	}

	private Organization getSelfOrganization() {

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + cashboxId);
		}
		Long organizationId = cashbox.getPaymentPoint().getCollector().getOrganization().getId();
		return organizationService.readFull(new Stub<Organization>(organizationId));
	}

	// updates status for selected operation and it's documents
	private void updateOperationStatus() throws FlexPayException {

		OperationStatus operationStatus = operationStatusService.read(Integer.parseInt(status));
		Operation operation = operationService.read(new Stub<Operation>(selectedOperationId));
		operation.setOperationStatus(operationStatus);

		if (operationStatus.getCode() == OperationStatus.REGISTERED) {
			operation.setRegisterDate(new Date());
			operation.setRegisterOrganization(getSelfOrganization());
			operation.setRegisterUserName(SecurityUtil.getUserName());
		}

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

		if (minimalSumm != null && maximalSumm != null && minimalSumm.compareTo(maximalSumm) > 0) {
			addActionError(getText("payments.error.operations.list.minimal_summ_must_be_not_greater_than_maximal"));
		}

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
		return "true".equals(documentSearchEnabled);
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

	public boolean processButtonsIsEmpty() {
		return processButtons.isEmpty();
	}

	public boolean isHighlighted(Document document) {
		return highlightedDocumentIds.contains(document.getId());
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

	public BeginTimeFilter getBeginTimeFilter() {
		return beginTimeFilter;
	}

	public EndTimeFilter getEndTimeFilter() {
		return endTimeFilter;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public String getMinimalSumm() {
		return minimalSumm != null ? minimalSumm.toString() : "";
	}

	public void setMinimalSumm(String minimalSumm) {
		try {
			this.minimalSumm = new BigDecimal(minimalSumm);
		} catch (NumberFormatException e) {
			log.warn("Minimal summ is not set because of bad string parameter value");
			this.minimalSumm = null;
		}
	}

	public String getMaximalSumm() {
		return maximalSumm != null ? maximalSumm.toString() : "";
	}

	public void setMaximalSumm(String maximalSumm) {
		try {
			this.maximalSumm = new BigDecimal(maximalSumm);
		} catch (NumberFormatException e) {
			log.warn("Maximal summ is not set because of bad string parameter value");
			this.maximalSumm = null;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setServiceTypeId(String serviceTypeId) {
		try {
			this.serviceTypeId = Long.parseLong(serviceTypeId);
		} catch (NumberFormatException e) {
			log.warn("Service type id is not set because of bad string parameter value");
			this.serviceTypeId = null;
		}
	}

	public String getServiceTypeId() {
		return serviceTypeId != null ? serviceTypeId.toString() : "";
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

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public List<String> getProcessButtons() {
		return processButtons;
	}

	public void setProcessButtons(List<String> processButtons) {
		this.processButtons = processButtons;
	}

	public Long getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(Long taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
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

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
