package org.flexpay.payments.service.statistics;

import org.flexpay.payments.service.Roles;
import org.springframework.security.annotation.Secured;

import java.util.Date;
import java.util.List;

public interface PaymentsStatisticsService {

	/**
	 * Collect statistics for paym,ents in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @return list of service statistics
	 */
	@Secured (Roles.PAYMENTS_REPORT)
	List<ServicePaymentsStatistics> servicePaymentStatistics(Date beginDate, Date endDate);
}
