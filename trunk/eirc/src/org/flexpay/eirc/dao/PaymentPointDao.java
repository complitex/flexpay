package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.PaymentPoint;

import java.util.List;

/**
 * Payment points dao interface
 */
public interface PaymentPointDao extends GenericDao<PaymentPoint, Long> {

	List<PaymentPoint> listPoints(Long townId, Page<PaymentPoint> pager);
}
