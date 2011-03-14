package org.flexpay.payments.action.reports;

import org.flexpay.common.util.SecurityUtil;
import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;

import java.util.Date;

public class DayReturnedPaymentsReportAction extends DayPaymentsReportAction {

	private static final String RETURNED_PAYMENTS_REPORT_NAME = "ReturnedPayments";
	private static final String RETURNED_PAYMENTS_SHORT_REPORT_NAME = "ReturnedPayments_short";


	@Override
	protected PaymentsPrintInfoData getPaymentsData(Date beginDate, Date endDate) {
		return paymentsReporter.getReturnedPaymentsPrintFormData(beginDate, endDate, getCashboxStub(), SecurityUtil.getUserName(), getUserPreferences().getLocale());
	}

	@Override
	protected String getReportBaseName() {
		return showDetails ? RETURNED_PAYMENTS_REPORT_NAME : RETURNED_PAYMENTS_SHORT_REPORT_NAME;
	}
}
