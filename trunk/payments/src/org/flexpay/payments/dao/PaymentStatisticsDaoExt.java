package org.flexpay.payments.dao;

import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.ServicePaymentsStatistics;

import java.util.Date;
import java.util.List;

public interface PaymentStatisticsDaoExt {

	/**
	 * Build payments statistics based on services
	 *
	 * @param begin Report period begin timestamp
	 * @param end   Report period end timestamp
	 * @return List of payment statistics
	 */
	List<ServicePaymentsStatistics> getServicePaymentStats(Date begin, Date end);

	/**
	 * Build payment operations statistics
	 *
	 * @param organizationId Register organization
	 * @param begin		  Report period begin timestamp
	 * @param end			Report period end timestamp
	 * @return List of payment operation statistics
	 */
	List<OperationTypeStatistics> getOperationTypeStatistics(Long organizationId, Date begin, Date end);

}
