package org.flexpay.payments.action.reports;

import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.reports.payments.AccPaymentsReportRequest;
import org.flexpay.payments.reports.payments.AccReportRequest;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.CollectionUtils.arrayStack;

public class AccReturnedPaymentsReportAction extends AccPaymentsReportAction {

	private static final String PREFIX = "AccReturned";

	private static final String ACC_RETURNED_ALL_PAYMENT_POINTS_NO_DETAILS = PREFIX + "AllPaymentPointsNoDetails";
	private static final String ACC_RETURNED_PAYMENT_POINT_NO_DETAILS = PREFIX + "PaymentPointNoDetails";

	private static final String ACC_RETURNED_ALL_PAYMENT_POINTS_CASHBOXES = PREFIX + "AllPaymentPointsCashboxes";
	private static final String ACC_RETURNED_PAYMENT_POINT_CASHBOXES = PREFIX + "PaymentPointCashboxes";
	private static final String ACC_RETURNED_CASHBOX = PREFIX + "Cashbox";

	private static final String ACC_RETURNED_ALL_PAYMENT_POINTS_PAYMENTS = PREFIX + "AllPaymentPointsPayments";
	private static final String ACC_RETURNED_PAYMENT_POINT_PAYMENTS = PREFIX + "PaymentPointPayments";
	private static final String ACC_RETURNED_CASHBOX_PAYMENTS = PREFIX + "CashboxPayments";

    private PaymentPointFilter paymentPointFilter = new PaymentPointFilter();
    private CashboxFilter cashboxFilter = new CashboxFilter();
    private Integer details;

    private PaymentPointService paymentPointService;
    private CashboxService cashboxService;

    @SuppressWarnings({"unchecked"})
    @Override
    protected void initFilters() {
        paymentPointFilter.initFilter(session);
        paymentPointService.initFilter(paymentPointFilter);

        if (paymentPointFilter.needFilter()) {
            cashboxFilter.initFilter(session);
            cashboxService.initFilter(arrayStack(paymentPointFilter), cashboxFilter);
        }
    }

    @Override
    protected AccReportRequest buildReportRequest() {

        AccPaymentsReportRequest request = new AccPaymentsReportRequest();

        request.setBeginDate(beginTimeFilter.setTime(beginDateFilter.getDate()));
        request.setEndDate(endTimeFilter.setTime(endDateFilter.getDate()));
        request.setPaymentStatus(OperationStatus.RETURNED);
        request.setLocale(getUserPreferences().getLocale());
        request.setPaymentCollectorId(((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId());

        request.setDetailsLevel(details);

        if (paymentPointFilter != null && paymentPointFilter.needFilter()) {
            request.setPaymentPointId(paymentPointFilter.getSelectedId());
        }

        if (cashboxFilter != null && cashboxFilter.needFilter()) {
            request.setCashboxId(cashboxFilter.getSelectedId());
        }

        return request;

    }

    @Override
    protected void uploadAdditionalReportFiles(AccReportRequest request) throws Exception {

        String reportName = getReportName(request);
        AccPaymentsReportRequest reportRequest = (AccPaymentsReportRequest) request;

        Long paymentPointId = reportRequest.getPaymentPointId();
        Long cashboxId = reportRequest.getCashboxId();
        switch (reportRequest.getDetailsLevel()) {
            case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT_POINT:
                break;
            case AccPaymentsReportRequest.DETAILS_LEVEL_CASHBOX:
                if (paymentPointId == null && cashboxId == null) {
                    uploadReport(reportName + CASHBOXES_SUFFIX);
                } else if (paymentPointId != null && cashboxId == null) {
                    uploadReport(reportName + CASHBOXES_SUFFIX);
                }
                break;
            case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT:
                if (paymentPointId == null && cashboxId == null) {
                    uploadReport(reportName + CASHBOXES_SUFFIX);
                    uploadReport(reportName + PAYMENTS_SUFFIX);
                } else if (paymentPointId != null && cashboxId == null) {
                    uploadReport(reportName + PAYMENTS_SUFFIX);
                } else if (paymentPointId != null) {
                    uploadReport(reportName + PAYMENTS_SUFFIX);
                }
                break;
            default:
                break;
        }

    }

	@Override
	protected String getReportName(AccReportRequest request) {

        AccPaymentsReportRequest reportRequest = (AccPaymentsReportRequest) request;

		Long paymentPointId = reportRequest.getPaymentPointId();
		Long cashboxId = reportRequest.getCashboxId();

		switch (reportRequest.getDetailsLevel()) {
			case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT_POINT:
				if (paymentPointId == null && cashboxId == null) {
					return ACC_RETURNED_ALL_PAYMENT_POINTS_NO_DETAILS;
				} else if (paymentPointId != null && cashboxId == null) {
					return ACC_RETURNED_PAYMENT_POINT_NO_DETAILS;
				}
				break;
			case AccPaymentsReportRequest.DETAILS_LEVEL_CASHBOX:
				if (paymentPointId == null && cashboxId == null) {
					return ACC_RETURNED_ALL_PAYMENT_POINTS_CASHBOXES;
				} else if (paymentPointId != null && cashboxId == null) {
					return ACC_RETURNED_PAYMENT_POINT_CASHBOXES;
				} else if (paymentPointId != null) {
					return ACC_RETURNED_CASHBOX;
				}
				break;
			case AccPaymentsReportRequest.DETAILS_LEVEL_PAYMENT:
				if (paymentPointId == null && cashboxId == null) {
					return ACC_RETURNED_ALL_PAYMENT_POINTS_PAYMENTS;
				} else if (paymentPointId != null && cashboxId == null) {
					return ACC_RETURNED_PAYMENT_POINT_PAYMENTS;
				} else if (paymentPointId != null) {
					return ACC_RETURNED_CASHBOX_PAYMENTS;
				}
				break;
			default:
				break;
		}

		return null;
	}

    public PaymentPointFilter getPaymentPointFilter() {
        return paymentPointFilter;
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

    public Integer getDetails() {
        return details;
    }

    public void setDetails(Integer details) {
        this.details = details;
    }

    @Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

}
