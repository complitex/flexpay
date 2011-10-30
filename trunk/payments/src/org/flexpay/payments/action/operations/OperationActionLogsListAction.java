package org.flexpay.payments.action.operations;

import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.service.OperationActionLogService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class OperationActionLogsListAction extends AccountantAWPWithPagerActionSupport<OperationActionLog> {

    private CashboxFilter cashboxFilter = new CashboxFilter();
    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();
    private BeginDateFilter beginDateFilter = new BeginDateFilter();
    private EndDateFilter endDateFilter = new EndDateFilter();
    private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
    private EndTimeFilter endTimeFilter = new EndTimeFilter();

    private List<OperationActionLog> operationActionLogs = list();

    private OperationActionLogService operationActionLogService;
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

        PaymentCollector paymentCollector = getPaymentCollector();

        PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter(paymentCollector.getId());

        if (log.isDebugEnabled()) {
            log.debug("beginDateFilter = {}, endDateFilter = {}", beginDateFilter, endDateFilter);
            log.debug("paymentCollectorId = {}, paymentPointId = {}, cashboxId = {}, serviceTypeId = {}", new Object[] {paymentCollectorFilter.getSelectedId(), paymentPointFilter.getSelectedId(), cashboxFilter.getSelectedId()});
        }

        List<OperationActionLog> operationActionLogs = operationActionLogService.searchOperationActionLogs(null,
                    arrayStack(paymentCollectorFilter, paymentPointFilter, cashboxFilter, beginDateFilter, endDateFilter), getPager());

        if (log.isDebugEnabled()) {
            log.debug("Found {} operation action logs", operationActionLogs.size());
        }

    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return ERROR;
    }

    public List<OperationActionLog> getOperationActionLogs() {
        return operationActionLogs;
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

    @Required
    public void setOperationActionLogService(OperationActionLogService operationActionLogService) {
        this.operationActionLogService = operationActionLogService;
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
