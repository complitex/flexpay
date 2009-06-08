package org.flexpay.payments.service.statistics.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.dao.PaymentStatisticsDaoExt;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.service.statistics.ServicePaymentsStatistics;
import org.flexpay.payments.persistence.Cashbox;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class PaymentsStatisticsServiceImpl implements PaymentsStatisticsService {

	private PaymentStatisticsDaoExt statisticsDao;

	/**
	 * Collect statistics for paym,ents in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @return list of service statistics
	 */
	public List<ServicePaymentsStatistics> servicePaymentStatistics(Date beginDate, Date endDate) {
		return statisticsDao.getServicePaymentStats(beginDate, endDate);
	}

	/**
	 * Collect statistics for payment operations in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param stub	  Organization stab to generate statistics for
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date   @return list of operation type statistics
     * @return
	 */
	public List<OperationTypeStatistics> operationTypeOrganizationStatistics(Stub<Organization> stub, Date beginDate, Date endDate) {
		return statisticsDao.getOperationTypeOrganizationStatistics(stub.getId(), beginDate, endDate);
	}

    /**
	 * Collect statistics for payment operations in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param stub Payment Point stab to generate statistics for
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date   @return list of operation type statistics
     * @return
	 */
    public List<OperationTypeStatistics> operationTypePaymentPointStatistics(Stub<PaymentPoint> stub, Date beginDate, Date endDate) {
        return statisticsDao.getOperationTypePaymentPointStatistics(stub.getId(), beginDate, endDate);
    }

     /**
	 * Collect statistics for payment operations in period between <code>beginDate</code> and <code>endDate</code>
	 * <p/>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param stub Cashbox stab to generate statistics for
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date   @return list of operation type statistics
     * @return
	 */
    public List<OperationTypeStatistics> operationTypeCashboxStatistics(Stub<Cashbox> stub, Date beginDate, Date endDate) {
        return statisticsDao.getOperationTypeCashboxStatistics(stub.getId(), beginDate, endDate);
    }

    public void setStatisticsDao(PaymentStatisticsDaoExt statisticsDao) {
		this.statisticsDao = statisticsDao;
	}
}
