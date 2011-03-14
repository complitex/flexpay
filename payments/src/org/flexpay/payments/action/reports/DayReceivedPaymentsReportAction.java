package org.flexpay.payments.action.reports;

import org.flexpay.common.util.SecurityUtil;
import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;

import java.util.Date;

public class DayReceivedPaymentsReportAction extends DayPaymentsReportAction {

	private static final String RECEIVED_PAYMENTS_REPORT_NAME = "ReceivedPayments";
	private static final String RECEIVED_PAYMENTS_SHORT_REPORT_NAME = "ReceivedPayments_short";

	@Override
	protected PaymentsPrintInfoData getPaymentsData(Date beginDate, Date endDate) {
		return paymentsReporter.getReceivedPaymentsPrintFormData(beginDate, endDate, getCashboxStub(), SecurityUtil.getUserName(), getUserPreferences().getLocale());
	}

	@Override
	protected String getReportBaseName() {
		return showDetails ? RECEIVED_PAYMENTS_REPORT_NAME : RECEIVED_PAYMENTS_SHORT_REPORT_NAME;
	}
}
