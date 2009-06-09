package org.flexpay.payments.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.CashboxDao;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class CashboxServiceImpl implements CashboxService {

	private CashboxDao cashboxDao;

	@Transactional (readOnly = false)
	public void create(Cashbox cashbox) throws FlexPayException {
		cashboxDao.create(cashbox);
	}

	@Transactional (readOnly = false)
	public void update(Cashbox cashbox) throws FlexPayException {
		cashboxDao.update(cashbox);
	}

	public Cashbox read(@NotNull Stub<Cashbox> stub) {
		return cashboxDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	public void delete(Cashbox cashbox) {
		cashboxDao.delete(cashbox);
	}

	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			Cashbox cashbox = cashboxDao.read(id);
			if (cashbox != null) {
				cashbox.disable();
				cashboxDao.update(cashbox);
			}
		}
	}

	public List<Cashbox> findObjects(Page<Cashbox> pager) {
		return cashboxDao.findCashboxes(pager);
	}

    public List<Cashbox> findCashboxesForPaymentPoint(Long paymentPointId) {
        return cashboxDao.findCashboxesForPaymentPoint(paymentPointId);
    }

	@Required
	public void setCashboxDao(CashboxDao cashboxDao) {
		this.cashboxDao = cashboxDao;
	}

}
