package org.flexpay.payments.dao.impl;

import org.flexpay.payments.dao.PaymentStatisticsDaoExt;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.ServicePaymentsStatistics;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class PaymentStatisticsDaoExtImpl extends HibernateDaoSupport implements PaymentStatisticsDaoExt {

	/**
	 * Build payments statistics based on services
	 *
	 * @param begin Report period begin timestamp
	 * @param end   Report period end timestamp
	 * @return List of payment statistics
	 */
	@SuppressWarnings ({"unchecked"})
    @Override
	public List<ServicePaymentsStatistics> getServicePaymentStats(Date begin, Date end) {
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("ServicePaymentsStatistics.collect", begin, end, begin, end);

		List<ServicePaymentsStatistics> result = list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			ServicePaymentsStatistics stats = new ServicePaymentsStatistics();
			stats.setOrganizationId((Long) row[0]);
			stats.setServiceId((Long) row[1]);
			stats.setPayedCacheSum((BigDecimal) row[2]);
			stats.setPayedCachelessSum(BigDecimal.ZERO);
			stats.setReturnedCacheSum(BigDecimal.ZERO);
			stats.setReturnedCachelessSum(BigDecimal.ZERO);
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
    @Override
	public List<OperationTypeStatistics> getOperationTypeOrganizationStatistics(Long organizationId, Date begin, Date end) {
		Object[] params = {organizationId, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("OperationTypeOrganizationStatistics.collect", params);

		List<OperationTypeStatistics> result = list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			OperationTypeStatistics stats = new OperationTypeStatistics();
			stats.setOperationTypeCode((Integer) row[0]);
			stats.setCount((Long) row[1]);
			stats.setSum((BigDecimal) row[2]);
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
    @Override
    public List<OperationTypeStatistics> getOperationTypePaymentPointStatistics(Long paymentPointId, Date begin, Date end) {
        Object[] params = {paymentPointId, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("OperationTypePaymentPointStatistics.collect", params);

		List<OperationTypeStatistics> result = list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			OperationTypeStatistics stats = new OperationTypeStatistics();
			stats.setOperationTypeCode((Integer) row[0]);
			stats.setCount((Long) row[1]);
			stats.setSum((BigDecimal) row[2]);
			result.add(stats);
		}

		return result;
    }

    /**
	 * Build payment operations statistics
	 *
	 * @param cashboxId Cashbox id
	 * @param begin		  Report period begin timestamp
	 * @param end			Report period end timestamp
	 * @return List of payment operation statistics
	 */
    @Override
    public List<OperationTypeStatistics> getOperationTypeCashboxStatistics(Long cashboxId, Date begin, Date end) {
        Object[] params = {cashboxId, begin, end};
		List<?> data = getHibernateTemplate()
				.findByNamedQuery("OperationTypeCashboxStatistics.collect", params);

		List<OperationTypeStatistics> result = list();
		for (Object obj : data) {
			Object[] row = (Object[]) obj;
			OperationTypeStatistics stats = new OperationTypeStatistics();
			stats.setOperationTypeCode((Integer) row[0]);
			stats.setCount((Long) row[1]);
			stats.setSum((BigDecimal) row[2]);
			result.add(stats);
		}

		return result;
    }
}
