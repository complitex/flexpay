package org.flexpay.payments.reports.payments;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.Roles;
import org.springframework.security.annotation.Secured;
import org.flexpay.orgs.persistence.PaymentPoint;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
	 */
	@Secured(Roles.PAYMENTS_REPORT)
	PaymentPrintForm getPaymentPrintFormData(Stub<Operation> stub);

	/**
	 * Get quittance payment print form data
	 *
	 * @param operation Payment operation to build form for
	 * @return PaymentPrintForm form data
	 */
	@Secured(Roles.PAYMENTS_REPORT)
	PaymentPrintForm getPaymentPrintFormData(Operation operation);

	/**
	 * Get received payments print form data for payment point
	 * @param begin begin date report parameter
	 * @param end end date report parameter
	 * @param paymentPoint payment point
	 * @param locale report locale
	 * @return printable form data
	 */
	@Secured(Roles.PAYMENTS_REPORT)
	PaymentsPrintInfoData getReceivedPaymentsPrintFormData(Date begin, Date end, PaymentPoint paymentPoint, Locale locale);

	/**
	 * Get printable information about returned payments in given period for payment point
	 * @param begin period begin date
	 * @param end period end date
	 * @param paymentPoint payment point
	 * @param locale report locale
	 * @return printable form data
	 */
	PaymentsPrintInfoData getReturnedPaymentsPrintFormData(Date begin, Date end, PaymentPoint paymentPoint, Locale locale);
}
