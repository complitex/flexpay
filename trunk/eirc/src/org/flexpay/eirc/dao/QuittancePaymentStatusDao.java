package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.QuittancePaymentStatus;

import java.util.List;

public interface QuittancePaymentStatusDao extends GenericDao<QuittancePaymentStatus, Long> {

	List<QuittancePaymentStatus> findStatus(int code);
}
