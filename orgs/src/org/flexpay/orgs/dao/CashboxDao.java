package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Cashbox;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CashboxDao extends GenericDao<Cashbox, Long> {

	List<Cashbox> findCashboxes(Page<Cashbox> pager);

	List<Cashbox> listCashboxes(@NotNull Long paymentPointId, Page<Cashbox> pager);

    List<Cashbox> findCashboxesForPaymentPoint(@NotNull Long paymentPointId);

    List<Cashbox> findCashboxesForPaymentCollector(@NotNull Long paymentCollectorId, Page<Cashbox> pager);

}
