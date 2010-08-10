package org.flexpay.payments.actions.operations;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.actions.OperatorAWPWithPagerActionSupport;
import org.flexpay.payments.actions.tradingday.TradingDayControlPanel;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.persistence.operation.sorter.OperationSorterById;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.getEndOfThisDay;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.payments.service.Roles.PAYMENTS_DEVELOPER;

public class OperationsListAction extends OperatorAWPWithPagerActionSupport<Operation> implements InitializingBean {

	private Cashbox cashbox = new Cashbox();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
	private BigDecimal minimalSum;
	private BigDecimal maximalSum;
	private Boolean documentSearch = false;
    private OperationSorterById operationSorterById = new OperationSorterById();

	private List<Operation> operations = list();
	private List<Long> highlightedDocumentIds = list();
	private TradingDayControlPanel tradingDayControlPanel;

	private DocumentService documentService;
	private OperationService operationService;
	private CurrencyInfoService currencyInfoService;
	private ProcessManager processManager;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (cashbox == null || cashbox.isNew()) {
			log.warn("Incorrect cashbox id {}", cashbox);
			addActionError(getText("payments.error.cashbox.incorrect_cashbox_id"));
			return ERROR;
		}

		Stub<Cashbox> stub = stub(cashbox);
		cashbox = cashboxService.read(stub);
		if (cashbox == null) {
			log.warn("Can't get cashbox with id {} from DB", stub.getId());
			addActionError(getText("payments.error.cashbox.cant_get_cashbox"));
			return ERROR;
		} else if (cashbox.isNotActive()) {
			log.warn("Cashbox with id {} is disabled", stub.getId());
			addActionError(getText("payments.error.cashbox.cant_get_cashbox"));
			return ERROR;
		}

		Stub<PaymentPoint> paymentPointStub = stub(cashbox.getPaymentPoint());
		PaymentPoint paymentPoint = paymentPointService.read(paymentPointStub);
		if (paymentPoint == null) {
			log.warn("Can't get payment point with id {} from DB", paymentPointStub.getId());
			addActionError(getText("payments.error.payment_point.cant_get_payment_point"));
			return ERROR;
		}

        PaymentCollector collector = paymentPoint.getCollector();
        if (collector == null) {
            addActionError(getText("payments.error.monitor.cant_get_payment_collector"));
            return ERROR;
        }

		tradingDayControlPanel.updatePanel(collector);

		if (!doValidate()) {
			return ERROR;
		}

		loadOperations(collector);

		return SUCCESS;
	}

	private boolean doValidate() {

		Date beginDate = beginDateFilter.getDate();
		Date endDate = endDateFilter.getDate();

		if (beginDate.after(endDate)) {
			addActionError(getText("payments.error.operations.list.begin_date_must_be_before_end_date"));
		}

		if (minimalSum != null && maximalSum != null && minimalSum.compareTo(maximalSum) > 0) {
			addActionError(getText("payments.error.operations.list.minimal_sum_must_be_not_greater_than_maximal"));
		}

		return !hasActionErrors();
	}

	private void loadOperations(@NotNull PaymentCollector collector) {

		List<Operation> searchResults = list();

        if (tradingDayControlPanel.isTradingDayOpened()) {

            if (documentSearch != null && documentSearch) {
                Date begin = beginDateFilter.getDate();
                Date end = getEndOfThisDay(endDateFilter.getDate());
                searchResults = operationService.searchDocuments(operationSorterById.isActivated() ? operationSorterById : null, stub(cashbox), serviceTypeFilter.getSelectedId(), begin, end, minimalSum, maximalSum, getPager());
                highlightedDocumentIds = list();

                for (Operation operation : searchResults) {
                    List<Document> docs = documentService.searchDocuments(stub(operation), serviceTypeFilter.getSelectedId(), minimalSum, maximalSum);
                    for (Document doc : docs) {
                        highlightedDocumentIds.add(doc.getId());
                    }
                }

            } else {
                Long tradingDayProcessId = collector.getTradingDayProcessInstanceId();
                Process tradingDayProcess = processManager.getProcessInstanceInfo(tradingDayProcessId);
                Date pStart = tradingDayProcess.getProcessStartDate();
                Date fStart = beginTimeFilter.setTime(now());
                Date end = endTimeFilter.setTime(now());
                Date begin = pStart.compareTo(fStart) > 0 ? pStart : fStart;
                if (SecurityUtil.isAuthenticationGranted(PAYMENTS_DEVELOPER)) {
                    begin = beginTimeFilter.setTime(beginDateFilter.getDate());
                    end = endTimeFilter.setTime(endDateFilter.getDate());
                    tradingDayProcessId = null;
                }
                if (log.isDebugEnabled()) {
                    log.debug("Start date={}, finish date={}", formatWithTime(begin), formatWithTime(end));
                    log.debug("Trading day process id = {}", tradingDayProcessId);
                }
                searchResults = operationService.searchOperations(operationSorterById.isActivated() ? operationSorterById : null,
                        tradingDayProcessId, stub(cashbox), begin, end, minimalSum, maximalSum, getPager());
            }
        } else {

            log.warn("Trading day process is closed for payment collector with id {}. Operation status update canceled", collector.getId());
            addActionMessage(getText("payments.quittance.payment.operation_changes_not_alowed_due_closed_trading_day"));

/*
            ProcessSorterByEndDate processSorter = new ProcessSorterByEndDate();
            processSorter.setOrder(ObjectSorter.ORDER_DESC);

            List<Process> processes = processManager.getProcesses(processSorter, new Page<Process>(1000), now(), new Date(), ProcessState.COMPLETED, TradingDay.PROCESS_DEFINITION_NAME);
            if (log.isDebugEnabled()) {
                log.debug("Found {} processes", processes.size());
            }

            if (!processes.isEmpty()) {

                Process tradingDayProcess = findTradingDayProcess(collector, processes);
                log.debug("Closed trading day process: {}", tradingDayProcess);

                if (tradingDayProcess != null) {

                    Date pStart = tradingDayProcess.getProcessStartDate();
                    Date pEnd = tradingDayProcess.getProcessEndDate();
                    Date fStart = beginTimeFilter.setTime(now());
                    Date fEnd = endTimeFilter.setTime(now());

                    Date begin = pStart.compareTo(fStart) > 0 ? pStart : fStart;
                    Date end = pEnd.compareTo(fEnd) < 0 ? pEnd : fEnd;

                    log.debug("Get operations for beginDate {} and endDate {}", begin, end);

                    searchResults = operationService.searchOperations(operationSorterById.isActivated() ? operationSorterById : null, null, stub(cashbox), begin, end, minimalSum, maximalSum, getPager());

                }
            }
*/
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} operations", searchResults.size());
        }

        operations = operationService.readFull(collectionIds(searchResults), true);
	}

    private Process findTradingDayProcess(PaymentCollector collector, List<Process> processes) {

        for (Process process : processes) {

            Process tradingDayProcess = processManager.getProcessInstanceInfo(process.getId());
            Long paymentCollectorId = (Long) tradingDayProcess.getParameters().get(ExportJobParameterNames.PAYMENT_COLLECTOR_ID);
            log.debug("Closed trading day process paymentCollectorId variable ({}) and this paymentCollector id ({})", paymentCollectorId, collector.getId());

            if (collector.getId().equals(paymentCollectorId)) {
                return tradingDayProcess;
            }
        }

        return null;
    }

	@NotNull
	@Override
	protected String getErrorResult() {
		return ERROR;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tradingDayControlPanel = new TradingDayControlPanel(processManager, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR, log);
	}

	public String getCurrencyName() {
		return currencyInfoService.getDefaultCurrency().getName(getUserPreferences().getLocale()).getShortName();
	}

	public BigDecimal getTotalPaymentsSum() {
		return getTotalSumForOperations(OperationStatus.REGISTERED);
	}

	public BigDecimal getTotalReturnsSum() {
		return getTotalSumForOperations(OperationStatus.RETURNED);
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

	public boolean isHighlighted(Document document) {
		return highlightedDocumentIds.contains(document.getId());
	}

	private BigDecimal getTotalSumForOperations(int statusCode) {

		BigDecimal sum = new BigDecimal("0.00");
		for (Operation operation : operations) {
			if (statusCode == operation.getOperationStatus().getCode()) {
				sum = sum.add(operation.getOperationSum());
			}
		}

		return sum;
	}

	public List<Long> getHighlightedDocumentIds() {
		return highlightedDocumentIds;
	}

	public Boolean isDocumentSearch() {
		return documentSearch;
	}

	public void setDocumentSearch(Boolean documentSearch) {
		this.documentSearch = documentSearch;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setMinimalSum(String minimalSum) {
		if (StringUtils.isEmpty(minimalSum)) {
			return;
		}
		try {
			this.minimalSum = new BigDecimal(minimalSum);
		} catch (NumberFormatException e) {
			log.warn("Minimal sum is not set because of bad string parameter value");
			this.minimalSum = null;
		}
	}

	public void setMaximalSum(String maximalSum) {
		if (StringUtils.isEmpty(maximalSum)) {
			return;
		}
		try {
			this.maximalSum = new BigDecimal(maximalSum);
		} catch (NumberFormatException e) {
			log.warn("Maximal sum is not set because of bad string parameter value");
			this.maximalSum = null;
		}
	}

    public OperationSorterById getOperationSorterById() {
        return operationSorterById;
    }

    public void setOperationSorterById(OperationSorterById operationSorterById) {
        this.operationSorterById = operationSorterById;
    }

    public Cashbox getCashbox() {
		return cashbox;
	}

	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	public void setBeginTimeFilter(BeginTimeFilter beginTimeFilter) {
		this.beginTimeFilter = beginTimeFilter;
	}

	public void setEndTimeFilter(EndTimeFilter endTimeFilter) {
		this.endTimeFilter = endTimeFilter;
	}

	public void setServiceTypeFilter(ServiceTypeFilter serviceTypeFilter) {
		this.serviceTypeFilter = serviceTypeFilter;
	}

	public TradingDayControlPanel getTradingDayControlPanel() {
		return tradingDayControlPanel;
	}

	@Required
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setCurrencyInfoService(CurrencyInfoService currencyInfoService) {
		this.currencyInfoService = currencyInfoService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
