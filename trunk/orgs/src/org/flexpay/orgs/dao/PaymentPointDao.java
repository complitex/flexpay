package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.PaymentPoint;

import java.util.List;

/**
 * Payment points dao interface
 */
public interface PaymentPointDao extends GenericDao<PaymentPoint, Long> {

    List<PaymentPoint> findByIdAndCollectorId(Long paymentPointId, Long paymentCollectorId);

	List<PaymentPoint> listPoints(Page<PaymentPoint> pager);

	List<PaymentPoint> listCollectorPoints(Long collectorId, Page<PaymentPoint> pager);

	List<PaymentPoint> listPoints();

    List<PaymentPoint> listPaymentPointsWithTradingDay();

    List<PaymentPoint> listPaymentPointsWithoutTradingDay();

}
