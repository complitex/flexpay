package org.flexpay.payments.service.statistics;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.service.Roles;
import org.springframework.security.annotation.Secured;

import java.util.Date;
import java.util.List;

public interface PaymentsStatisticsService {

	/**
	 * Collect statistics for payments in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @return list of service statistics
	 */
	@Secured (Roles.PAYMENTS_REPORT)
	List<ServicePaymentsStatistics> servicePaymentStatistics(Date beginDate, Date endDate);

	/**
	 * Collect statistics for payment operations in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param stub Organization stab to generate statistics for
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date   @return list of operation type statistics
	 */
	@Secured (Roles.PAYMENTS_REPORT)
	List<OperationTypeStatistics> operationTypeStatistics(Stub<Organization> stub, Date beginDate, Date endDate);
}
