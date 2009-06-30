package org.flexpay.payments.actions.reports;

import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;

import java.util.Date;

public class ReturnedPaymentsReportAction extends PaymentsDayReportAction {

	private static final String RETURNED_PAYMENTS_REPORT_NAME = "ReturnedPayments";
	private static final String RETURNED_PAYMENTS_SHORT_REPORT_NAME = "ReturnedPayments_short";


	/**
	 * {@inheritDoc}
	 */
	protected PaymentsPrintInfoData getPaymentsData(Date beginDate, Date endDate) {

		return paymentsReporter.getReturnedPaymentsPrintFormData(beginDate, endDate, getCashbox(), userPreferences.getLocale());
	}

	protected String getReportBaseName() {
		return showDetails ? RETURNED_PAYMENTS_REPORT_NAME : RETURNED_PAYMENTS_SHORT_REPORT_NAME;
	}
}
