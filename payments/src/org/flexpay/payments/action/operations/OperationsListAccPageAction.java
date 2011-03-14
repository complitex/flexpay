package org.flexpay.payments.action.operations;

import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.DateUtil.now;

public class OperationsListAccPageAction extends AccountantAWPActionSupport {

    private CashboxFilter cashboxFilter = new CashboxFilter();
    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();
    private BeginDateFilter beginDateFilter = new BeginDateFilter();
    private EndDateFilter endDateFilter = new EndDateFilter();
    private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
    private EndTimeFilter endTimeFilter = new EndTimeFilter();
    private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();

    private ServiceTypeService serviceTypeService;
    private CashboxService cashboxService;
    private PaymentPointService paymentPointService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        initFilters();

        return SUCCESS;
    }

    private void initFilters() {
        beginDateFilter.setDate(now());
        endDateFilter.setDate(now());

        serviceTypeService.initFilter(serviceTypeFilter);
        serviceTypeFilter.setDisabled(true);
        cashboxService.initFilter(cashboxFilter);
        cashboxFilter.setDisabled(true);
        PaymentCollector paymentCollector = getPaymentCollector();
        if (paymentCollector == null) {
            addActionError("");
            log.debug("Payment collector for this user is undefined. Can't initialize filters");
            return;
        }
        paymentPointService.initFilter(arrayStack(new PaymentCollectorFilter(paymentCollector.getId())), paymentPointFilter);
        paymentPointFilter.setReadOnly(false);
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public CashboxFilter getCashboxFilter() {
        return cashboxFilter;
    }

    public PaymentPointFilter getPaymentPointFilter() {
        return paymentPointFilter;
    }

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

    public ServiceTypeFilter getServiceTypeFilter() {
        return serviceTypeFilter;
    }

    @Required
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
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
