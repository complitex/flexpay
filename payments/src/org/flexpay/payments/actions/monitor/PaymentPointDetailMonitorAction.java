package org.flexpay.payments.actions.monitor;

import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PaymentPointDetailMonitorAction extends CashboxCookieActionSupport {
    private String detail;
    private String selectedPaymentPointName;
    private String updated;
    private String filter;
    private String totalSum;
    private String status;
    private List<String> dayClosed;
    private List<PaymentPointMonitorContainer> paymentPoints;
    private PaymentPointsFilter paymentPointsFilter;

    @NotNull
    protected String doExecute() throws Exception {
        return SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return SUCCESS;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSelectedPaymentPointName() {
        return selectedPaymentPointName;
    }

    public void setSelectedPaymentPointName(String selectedPaymentPointName) {
        this.selectedPaymentPointName = selectedPaymentPointName;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getDayClosed() {
        return dayClosed;
    }

    public void setDayClosed(List<String> dayClosed) {
        this.dayClosed = dayClosed;
    }

    public List<PaymentPointMonitorContainer> getPaymentPoints() {
        return paymentPoints;
    }

    public void setPaymentPoints(List<PaymentPointMonitorContainer> paymentPoints) {
        this.paymentPoints = paymentPoints;
    }

    public PaymentPointsFilter getPaymentPointsFilter() {
        return paymentPointsFilter;
    }

    public void setPaymentPointsFilter(PaymentPointsFilter paymentPointsFilter) {
        this.paymentPointsFilter = paymentPointsFilter;
    }

    @Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
        paymentPointsFilter = new PaymentPointsFilter();
        paymentPointService.initFilter(paymentPointsFilter);
	}
}
