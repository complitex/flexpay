package org.flexpay.payments.reports.payments;

import org.springframework.security.annotation.Secured;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.common.persistence.Stub;

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
	@Secured(Roles.PAYMENTS_REPORT)
	List<PaymentReportData> getPaymentsData(Date begin, Date end);

	/**
	 * Get quittance payment print form data
	 *
	 * @param stub Payment operation to build form for
	 * @return PaymentPrintForm form data
	 * @throws IllegalArgumentException if Operation reference is invalid
	 */
	@Secured(Roles.PAYMENTS_REPORT)
	PaymentPrintForm getPaymentPrintFormData(Stub<Operation> stub) throws IllegalArgumentException;
}
