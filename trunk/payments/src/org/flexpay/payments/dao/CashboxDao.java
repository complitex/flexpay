package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.Cashbox;

import java.util.List;

public interface CashboxDao extends GenericDao<Cashbox, Long> {

	List<Cashbox> findCashboxes(Page<Cashbox> pager);

}
