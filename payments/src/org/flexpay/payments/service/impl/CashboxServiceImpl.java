package org.flexpay.payments.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.payments.dao.CashboxDao;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.persistence.CashboxNameTranslation;
import org.flexpay.payments.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class CashboxServiceImpl implements CashboxService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CashboxDao cashboxDao;

	private SessionUtils sessionUtils;
	private ModificationListener<Cashbox> modificationListener;

	@NotNull
	@Transactional (readOnly = false)
	public Cashbox create(@NotNull Cashbox cashbox) throws FlexPayExceptionContainer {
		validate(cashbox);
		cashbox.setId(null);
		cashboxDao.create(cashbox);

		modificationListener.onCreate(cashbox);

		return cashbox;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Transactional (readOnly = false)
	public Cashbox update(@NotNull Cashbox cashbox) throws FlexPayExceptionContainer {
		validate(cashbox);

		if (cashbox.isNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("New", "common.error.update_new"));
		}

		Cashbox old = read(Stub.stub(cashbox));
		if (old == null) {
			throw new FlexPayExceptionContainer(new FlexPayException("No object found to update " + cashbox));
		}

		sessionUtils.evict(old);
		modificationListener.onUpdate(old, cashbox);

		cashboxDao.update(cashbox);

		return cashbox;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	public void validate(Cashbox cashbox) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (cashbox.getPaymentPoint() == null) {
			ex.addException(new FlexPayException("No payment point", "payments.error.cashbox.no_pp"));
		}

		boolean defaultNameFound = false;
		for (CashboxNameTranslation translation : cashbox.getNames()) {
			log.debug("Validating translation: {}", translation);
			boolean nameBlank = StringUtils.isBlank(translation.getName());
			if (translation.getLang().isDefault() && !nameBlank) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			ex.addException(new FlexPayException(
					"No default lang desc", "payments.error.cashbox.no_default_lang_name"));
		}

		if (ex.isNotEmpty()) {
			ex.debug(log);
			throw ex;
		}
	}

	public Cashbox read(@NotNull Stub<Cashbox> stub) {
		return cashboxDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	public void delete(@NotNull Cashbox cashbox) {
		cashboxDao.delete(cashbox);
	}

	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			Cashbox cashbox = cashboxDao.read(id);
			if (cashbox != null) {
				cashbox.disable();
				cashboxDao.update(cashbox);

				modificationListener.onDelete(cashbox);
				log.debug("Disabled object: {}", cashbox);
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

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Cashbox> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
