package org.flexpay.orgs.action.filter;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;

public class CashboxFilterAjaxAction extends FPActionSupport {

    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();
    private CashboxFilter cashboxFilter = new CashboxFilter();

    private CashboxService cashboxService;

    @NotNull
    @Override
    public String doExecute() throws FlexPayException {

        log.debug("Payment point filter = {}", paymentPointFilter);
        log.debug("Cashbox filter = {}", cashboxFilter);

        List<Cashbox> cashboxes = cashboxService.listCashboxes(arrayStack(paymentPointFilter), new Page<Cashbox>(1000));
        if (log.isDebugEnabled()) {
            log.debug("Found cashboxes: {}", cashboxes.size());
        }

        cashboxFilter.setCashboxes(cashboxes);
        cashboxFilter.setNeedAutoChange(false);

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
        this.paymentPointFilter = paymentPointFilter;
    }

    public CashboxFilter getCashboxFilter() {
        return cashboxFilter;
    }

    public void setCashboxFilter(CashboxFilter cashboxFilter) {
        this.cashboxFilter = cashboxFilter;
    }

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }
}
