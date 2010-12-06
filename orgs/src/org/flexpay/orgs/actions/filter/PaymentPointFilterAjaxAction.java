package org.flexpay.orgs.actions.filter;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;

public class PaymentPointFilterAjaxAction extends FPActionSupport {

    private PaymentCollectorFilter paymentCollectorFilter = new PaymentCollectorFilter();
    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();

    private PaymentPointService paymentPointService;

    @NotNull
    @Override
    public String doExecute() throws FlexPayException {

        log.debug("Payment collector filter = {}", paymentCollectorFilter);
        log.debug("Payment point filter = {}", paymentPointFilter);

        List<PaymentPoint> paymentPoints = paymentPointService.listCollectorPoints(arrayStack(paymentCollectorFilter), new Page<PaymentPoint>(1000));
        if (log.isDebugEnabled()) {
            log.debug("Found payment points: {}", paymentPoints.size());
        }

        paymentPointFilter.setPoints(paymentPoints);
        paymentPointFilter.setNeedAutoChange(false);

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public void setPaymentCollectorFilter(PaymentCollectorFilter paymentCollectorFilter) {
        this.paymentCollectorFilter = paymentCollectorFilter;
    }

    public PaymentPointFilter getPaymentPointFilter() {
        return paymentPointFilter;
    }

    public void setPaymentPointFilter(PaymentPointFilter paymentPointFilter) {
        this.paymentPointFilter = paymentPointFilter;
    }

    @Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }
}
