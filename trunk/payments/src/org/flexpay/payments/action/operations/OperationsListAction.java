package org.flexpay.payments.action.operations;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.payments.action.OperatorAWPWithPagerActionSupport;
import org.flexpay.payments.action.tradingday.TradingDayControlPanel;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.filters.CashboxTradingDayFilter;
import org.flexpay.payments.persistence.filters.MaximalSumFilter;
import org.flexpay.payments.persistence.filters.MinimalSumFilter;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.persistence.operation.sorter.OperationSorterById;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.getEndOfThisDay;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;
import static org.flexpay.payments.service.Roles.PAYMENTS_DEVELOPER;
import static org.flexpay.payments.service.Roles.TRADING_DAY_ACCOUNTER_ACTION;

public class OperationsListAction extends OperatorAWPWithPagerActionSupport<Operation> implements InitializingBean {

	private Cashbox cashbox = new Cashbox();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
    private MinimalSumFilter minimalSumFilter = new MinimalSumFilter();
    private MaximalSumFilter maximalSumFilter = new MaximalSumFilter();
	private Boolean documentSearch = false;
    private OperationSorterById operationSorterById = new OperationSorterById();

	private List<Operation> operations = list();
	private List<Long> highlightedDocumentIds = list();
	private TradingDayControlPanel tradingDayControlPanel;

	private DocumentService documentService;
	private OperationService operationService;
	private CurrencyInfoService currencyInfoService;
	private ProcessManager processManager;
	private TradingDay<Cashbox> cashboxTradingDayService;

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

        tradingDayControlPanel.updatePanel(cashbox.getTradingDayProcessInstanceId());

        if (!doValidate()) {
            return ERROR;
        }

        loadOperations();

		return SUCCESS;
	}

	private boolean doValidate() {

		Date beginDate = beginDateFilter.getDate();
		Date endDate = endDateFilter.getDate();

		if (beginDate.after(endDate)) {
			addActionError(getText("payments.error.operations.list.begin_date_must_be_before_end_date"));
		}

        if (minimalSumFilter != null && maximalSumFilter != null && minimalSumFilter.getBdValue() != null && maximalSumFilter.getBdValue() != null
                && minimalSumFilter.getBdValue().compareTo(maximalSumFilter.getBdValue()) > 0) {
            addActionError(getText("payments.error.operations.list.minimal_sum_must_be_not_greater_than_maximal"));
            log.debug("Incorrect minimalSum or maximalSum filter value (minS = {}, maxS = {})", minimalSumFilter.getBdValue(), maximalSumFilter.getBdValue());
        }

		return !hasActionErrors();
	}

	private void loadOperations() {

        if (log.isDebugEnabled()) {
            log.debug("minimaSumFilter = {}, maximalSumFilter = {}", minimalSumFilter, maximalSumFilter);
            log.debug("cashboxId = {}, serviceTypeId = {}", cashbox.getId(), serviceTypeFilter.getSelectedId());
        }

		List<Operation> searchResults = list();

        if (tradingDayControlPanel.isTradingDayOpened()) {

            log.debug("Get trading day operations (id = {})", cashbox.getTradingDayProcessInstanceId());

            if (documentSearch != null && documentSearch) {
                Date end = getEndOfThisDay(endDateFilter.getDate());

                if (log.isDebugEnabled()) {
                    log.debug("beginDateFilter = {}, endDateFilter = {}", formatWithTime(beginDateFilter.getDate()), formatWithTime(end));
                }

                searchResults = operationService.searchDocuments(operationSorterById.isActivated() ? operationSorterById : null,
                                        arrayStack(new CashboxFilter(cashbox), serviceTypeFilter, beginDateFilter, new EndDateFilter(end),
                                                minimalSumFilter, maximalSumFilter), getPager());
                highlightedDocumentIds = list();

                for (Operation operation : searchResults) {
                    List<Document> docs = documentService.searchDocuments(stub(operation), arrayStack(serviceTypeFilter, minimalSumFilter, maximalSumFilter));
                    for (Document doc : docs) {
                        highlightedDocumentIds.add(doc.getId());
                    }
                }

            } else {
                Long tradingDayProcessId = cashbox.getTradingDayProcessInstanceId();
                ProcessInstance tradingDayProcess = processManager.getProcessInstance(tradingDayProcessId);
                Date pStart = tradingDayProcess != null? tradingDayProcess.getStartDate(): null;
                Date fStart = beginTimeFilter.setTime(now());
                Date end = endTimeFilter.setTime(now());
                Date begin = fStart.compareTo(pStart) > 0 ? fStart : pStart;
                if (isAuthenticationGranted(PAYMENTS_DEVELOPER)) {
                    begin = beginTimeFilter.setTime(beginDateFilter.getDate());
                    end = endTimeFilter.setTime(endDateFilter.getDate());
                    tradingDayProcessId = null;
                }
                if (log.isDebugEnabled()) {
                    log.debug("Start date={}, finish date={}", formatWithTime(begin), formatWithTime(end));
                    log.debug("Trading day process id = {}", tradingDayProcessId);
                }
                searchResults = operationService.searchOperations(operationSorterById.isActivated() ? operationSorterById : null,
                        arrayStack(new CashboxTradingDayFilter(tradingDayProcessId), new CashboxFilter(cashbox), new BeginDateFilter(begin), new EndDateFilter(end),
                                                minimalSumFilter, maximalSumFilter), getPager());
            }
        } else if (cashbox.getTradingDayProcessInstanceId() != null) {

			ProcessInstance tradingDayProcess = processManager.getProcessInstance(cashbox.getTradingDayProcessInstanceId());
			log.debug("Closed trading day process: {}", tradingDayProcess);

			if (tradingDayProcess != null) {

                Date pStart;
                Date pEnd;
                try {
                    pStart = tradingDayProcess.getStartDate() == null ? DateUtil.parseDate("2000-01-01") : tradingDayProcess.getStartDate();
                    pEnd = tradingDayProcess.getEndDate() == null ? DateUtil.parseDate("2100-12-31") : tradingDayProcess.getEndDate();
                } catch (ParseException e) {
                    log.error("Can't parse date", e);
                    return;
                }
				Date fStart = beginTimeFilter.setTime(now());
				Date fEnd = endTimeFilter.setTime(now());

				Date begin = pStart.compareTo(fStart) > 0 ? pStart : fStart;
				Date end = pEnd.compareTo(fEnd) < 0 ? pEnd : fEnd;

				log.debug("Get operations for beginDate {} and endDate {}", begin, end);

				searchResults = operationService.searchOperations(operationSorterById.isActivated() ? operationSorterById : null,
                        arrayStack(new CashboxFilter(cashbox), new BeginDateFilter(begin), new EndDateFilter(end), minimalSumFilter, maximalSumFilter), getPager());

			}
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} operations", searchResults.size());
        }

        operations = operationService.readFull(collectionIds(searchResults), true);
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return ERROR;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
        if (isAuthenticationGranted(TRADING_DAY_ACCOUNTER_ACTION)) {
            tradingDayControlPanel = new TradingDayControlPanel(processManager, cashboxTradingDayService, AccounterAssignmentHandler.ACCOUNTER, log);
        } else {
            tradingDayControlPanel = new TradingDayControlPanel(processManager, cashboxTradingDayService, PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR, log);
        }
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

    public void setMinimalSumFilter(MinimalSumFilter minimalSumFilter) {
        this.minimalSumFilter = minimalSumFilter;
    }

    public void setMaximalSumFilter(MaximalSumFilter maximalSumFilter) {
        this.maximalSumFilter = maximalSumFilter;
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

	@Required
	public void setCashboxTradingDayService(TradingDay<Cashbox> cashboxTradingDayService) {
		this.cashboxTradingDayService = cashboxTradingDayService;
	}
}
