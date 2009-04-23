package org.flexpay.payments.reports.payments;

import org.springframework.security.annotation.Secured;
import org.flexpay.payments.service.Roles;

import java.util.List;
import java.util.Date;

public interface PaymentsReporter {

	/**
	 * Prepare data for payments report
	 *
	 * @param begin report period begin timestamp
	 * @param end report period end timestamp
	 * @return List of payment report data
	 */
//	@Secured(Roles.PAYMENTS_REPORT)
	List<PaymentReportData> getPaymentsData(Date begin, Date end);
}
