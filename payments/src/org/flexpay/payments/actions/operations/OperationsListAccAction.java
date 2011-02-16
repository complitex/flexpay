package org.flexpay.payments.actions.operations;

import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.filters.MaximalSumFilter;
import org.flexpay.payments.persistence.filters.MinimalSumFilter;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.persistence.operation.sorter.OperationSorterById;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class OperationsListAccAction extends AccountantAWPWithPagerActionSupport<Operation> {

    private CashboxFilter cashboxFilter = new CashboxFilter();
    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();
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

    private DocumentService documentService;
    private OperationService operationService;
    private CurrencyInfoService currencyInfoService;
    private CashboxService cashboxService;
    private PaymentPointService paymentPointService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (getPaymentCollector() == null) {
            log.warn("Incorrect payment collector in user preferences");
            addActionError(getText("payments.error.payment_collector_not_found"));
            return ERROR;
        }

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
            log.debug("Incorrect beginDate or endDate filter value (beginD = {}, endD = {})", beginDate, endDate);
        }

        if (minimalSumFilter != null && maximalSumFilter != null && minimalSumFilter.getBdValue() != null && maximalSumFilter.getBdValue() != null
                && minimalSumFilter.getBdValue().compareTo(maximalSumFilter.getBdValue()) > 0) {
            addActionError(getText("payments.error.operations.list.minimal_sum_must_be_not_greater_than_maximal"));
            log.debug("Incorrect minimalSum or maximalSum filter value (minS = {}, maxS = {})", minimalSumFilter.getBdValue(), maximalSumFilter.getBdValue());
        }

        if (cashboxFilter != null && cashboxFilter.getSelectedId() != null && cashboxFilter.getSelectedId() > 0) {
            Cashbox cashbox = cashboxService.read(cashboxFilter.getSelectedStub());
            if (cashbox == null || cashbox.isNotActive()) {
                addActionError(getText("admin.error.payment.incorrect_cashbox_id"));
                log.debug("Incorrect cashbox value ({})", cashboxFilter.getSelectedId());
            }
        } else if (paymentPointFilter != null && paymentPointFilter.getSelectedId() != null && paymentPointFilter.getSelectedId() > 0) {
            PaymentPoint paymentPoint = paymentPointService.read(paymentPointFilter.getSelectedStub());
            if (paymentPoint == null || paymentPoint.isNotActive()) {
                addActionError(getText("admin.error.payment.incorrect_payment_point_id"));
                log.debug("Incorrect payment point value ({})", paymentPointFilter);
            }
        }

        return !hasActionErrors();
    }

    private void loadOperations() {

        beginDateFilter.setDate(beginTimeFilter.setTime(beginDateFilter.getDate()));
        endDateFilter.setDate(endTimeFilter.setTime(endDateFilter.getDate()));

        if (log.isDebugEnabled()) {
            log.debug("beginDateFilter = {}, endDateFilter = {}", beginDateFilter, endDateFilter);
            log.debug("minimaSumFilter = {}, maximalSumFilter = {}", minimalSumFilter, maximalSumFilter);
            log.debug("paymentPointId = {}, cashboxId = {}, serviceTypeId = {}", new Object[] {paymentPointFilter.getSelectedId(), cashboxFilter.getSelectedId(), serviceTypeFilter.getSelectedId()});
        }

        List<Operation> searchResults;

        if (documentSearch != null && documentSearch) {
            searchResults = operationService.searchDocuments(operationSorterById.isActivated() ? operationSorterById : null,
                                    arrayStack(paymentPointFilter, cashboxFilter, beginDateFilter, endDateFilter, minimalSumFilter, maximalSumFilter), getPager());
            highlightedDocumentIds = list();

            for (Operation operation : searchResults) {
                List<Document> docs = documentService.searchDocuments(stub(operation), arrayStack(serviceTypeFilter, minimalSumFilter, maximalSumFilter));
                for (Document doc : docs) {
                    highlightedDocumentIds.add(doc.getId());
                }
            }
        } else {
            searchResults = operationService.searchOperations(operationSorterById.isActivated() ? operationSorterById : null,
                    arrayStack(paymentPointFilter, cashboxFilter, beginDateFilter, endDateFilter, minimalSumFilter, maximalSumFilter), getPager());
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

    public Boolean getDocumentSearch() {
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

    public void setCashboxFilter(CashboxFilter cashboxFilter) {
        this.cashboxFilter = cashboxFilter;
    }

    public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
        this.paymentPointFilter = paymentPointFilter;
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
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }
}
