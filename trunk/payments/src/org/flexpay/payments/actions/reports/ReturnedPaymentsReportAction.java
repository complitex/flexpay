package org.flexpay.payments.actions.reports;

import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;

import java.util.Date;

public class ReturnedPaymentsReportAction extends PaymentsDayReportAction {

	private static final String RETURNED_PAYMENTS_REPORT_NAME = "ReturnedPayments";

	/**
	 * {@inheritDoc}
	 */
	protected PaymentsPrintInfoData getPaymentsData(Date beginDate, Date endDate) {

		return paymentsReporter.getReturnedPaymentsPrintFormData(beginDate, endDate, getPaymentPoint(), userPreferences.getLocale());
	}

	protected String getReportBaseName() {
		return RETURNED_PAYMENTS_REPORT_NAME;
	}
}
