package org.flexpay.payments.dao.impl;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.dao.PaymentStatisticsDaoExt;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.ServicePaymentsStatistics;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PaymentStatisticsDaoExtImpl extends HibernateDaoSupport implements PaymentStatisticsDaoExt {

	/**
	 * Build payments statistics based on services
	 *
	 * @param begin Report period begin timestamp
	 * @param end   Report period end timestamp
	 * @return List of payment statistics
	 */
	@SuppressWarnings ({"unchecked"})
	public List<ServicePaymentsStatistics> getServicePaymentStats(Date begin, Date end) {
		Date[] params = {begin, end, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("ServicePaymentsStatistics.collect", params);

		List<ServicePaymentsStatistics> result = CollectionUtils.list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			ServicePaymentsStatistics stats = new ServicePaymentsStatistics();
			stats.setOrganizationId((Long) row[0]);
			stats.setServiceId((Long) row[1]);
			stats.setPayedCacheSumm((BigDecimal) row[2]);
			stats.setPayedCachelessSumm(BigDecimal.ZERO);
			stats.setReturnedCacheSumm(BigDecimal.ZERO);
			stats.setReturnedCachelessSumm(BigDecimal.ZERO);
			result.add(stats);
		}

		return result;
	}

	/**
	 * Build payment operations statistics
	 *
	 * @param organizationId Register organization
	 * @param begin		  Report period begin timestamp
	 * @param end			Report period end timestamp
	 * @return List of payment operation statistics
	 */
	public List<OperationTypeStatistics> getOperationTypeOrganizationStatistics(Long organizationId, Date begin, Date end) {
		Object[] params = {organizationId, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("OperationTypeOrganizationStatistics.collect", params);

		List<OperationTypeStatistics> result = CollectionUtils.list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			OperationTypeStatistics stats = new OperationTypeStatistics();
			stats.setOperationTypeCode((Integer) row[0]);
			stats.setCount((Long) row[1]);
			stats.setSumm((BigDecimal) row[2]);
			result.add(stats);
		}

		return result;
	}

    /**
	 * Build payment operations statistics
	 *
	 * @param paymentPointId Payment point
	 * @param begin		  Report period begin timestamp
	 * @param end			Report period end timestamp
	 * @return List of payment operation statistics
	 */
    public List<OperationTypeStatistics> getOperationTypePaymentPointStatistics(Long paymentPointId, Date begin, Date end) {
        Object[] params = {paymentPointId, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("OperationTypePaymentPointStatistics.collect", params);

		List<OperationTypeStatistics> result = CollectionUtils.list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			OperationTypeStatistics stats = new OperationTypeStatistics();
			stats.setOperationTypeCode((Integer) row[0]);
			stats.setCount((Long) row[1]);
			stats.setSumm((BigDecimal) row[2]);
			result.add(stats);
		}

		return result;
    }

        /**
	 * Build payment operations statistics
	 *
	 * @param cashboxId Cash box
	 * @param begin		  Report period begin timestamp
	 * @param end			Report period end timestamp
	 * @return List of payment operation statistics
	 */
    public List<OperationTypeStatistics> getOperationTypeCashboxStatistics(Long cashboxId, Date begin, Date end) {
        Object[] params = {cashboxId, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("OperationTypeCashboxStatistics.collect", params);

		List<OperationTypeStatistics> result = CollectionUtils.list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			OperationTypeStatistics stats = new OperationTypeStatistics();
			stats.setOperationTypeCode((Integer) row[0]);
			stats.setCount((Long) row[1]);
			stats.setSumm((BigDecimal) row[2]);
			result.add(stats);
		}

		return result;
    }
}
