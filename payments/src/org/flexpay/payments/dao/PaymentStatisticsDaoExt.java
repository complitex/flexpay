package org.flexpay.payments.dao;

import org.flexpay.payments.service.statistics.ServicePaymentsStatistics;

import java.util.List;
import java.util.Date;

public interface PaymentStatisticsDaoExt {

	/**
	 * Build payments statistics based on services
	 *
	 * @param begin Report period begin timestamp
	 * @param end Report period end timestamp
	 * @return List of payment statistics
	 */
	List<ServicePaymentsStatistics> getServicePaymentStats(Date begin, Date end);
}
