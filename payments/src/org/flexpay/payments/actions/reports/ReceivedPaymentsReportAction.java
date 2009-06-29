package org.flexpay.payments.actions.reports;

import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;

import java.util.Date;

public class ReceivedPaymentsReportAction extends PaymentsDayReportAction {

	private static final String RECEIVED_PAYMENTS_REPORT_NAME = "ReceivedPayments";
	private static final String RECEIVED_PAYMENTS_SHORT_REPORT_NAME = "ReceivedPayments_short";

	/**
	 * {@inheritDoc}
	 */
	protected PaymentsPrintInfoData getPaymentsData(Date beginDate, Date endDate) {

		return paymentsReporter.getReceivedPaymentsPrintFormData(beginDate, endDate, getPaymentPoint(), userPreferences.getLocale());
	}

	protected String getReportBaseName() {
		return showDetails ? RECEIVED_PAYMENTS_REPORT_NAME : RECEIVED_PAYMENTS_SHORT_REPORT_NAME;
	}
}
