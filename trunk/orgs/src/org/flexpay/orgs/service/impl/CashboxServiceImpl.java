package org.flexpay.orgs.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.dao.CashboxDao;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.CashboxNameTranslation;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.filters.CashboxFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class CashboxServiceImpl implements CashboxService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CashboxDao cashboxDao;

	private SessionUtils sessionUtils;
	private ModificationListener<Cashbox> modificationListener;

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	@Transactional (readOnly = false)
	public Cashbox create(@NotNull Cashbox cashbox) throws FlexPayExceptionContainer {
		validate(cashbox);
		cashbox.setId(null);
		cashboxDao.create(cashbox);

		modificationListener.onCreate(cashbox);

		return cashbox;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Override
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
	private void validate(Cashbox cashbox) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (cashbox.getPaymentPoint() == null) {
			ex.addException(new FlexPayException("No payment point", "orgs.error.cashbox.no_pp"));
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
					"No default lang desc", "orgs.error.cashbox.no_default_lang_name"));
		}

		if (ex.isNotEmpty()) {
			ex.debug(log);
			throw ex;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cashbox read(@NotNull Stub<Cashbox> stub) {
		return cashboxDao.readFull(stub.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<Cashbox> readFull(Collection<Long> ids, boolean preserveOrder) {
		return cashboxDao.readFullCollection(ids, preserveOrder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional (readOnly = false)
	public void delete(@NotNull Cashbox cashbox) {
		cashboxDao.delete(cashbox);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			Cashbox cashbox = cashboxDao.read(id);
			if (cashbox != null) {
				cashbox.disable();
				cashboxDao.update(cashbox);

				modificationListener.onDelete(cashbox);
				log.debug("Disabled: {}", cashbox);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public CashboxFilter initFilter(@NotNull CashboxFilter cashboxFilter) {
		return initFilter(CollectionUtils.arrayStack(), cashboxFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public CashboxFilter initFilter(@NotNull ArrayStack filters, @NotNull CashboxFilter cashboxFilter) {
		List<Cashbox> cashboxes = listCashboxes(filters);
		cashboxFilter.setCashboxes(cashboxes);
		return cashboxFilter;
	}

	private List<Cashbox> listCashboxes(ArrayStack filters) {

		if (filters.isEmpty()) {
			return cashboxDao.findCashboxes(new Page<Cashbox>(10000, 1));
		}

		// check if payments collector filter is there
		ObjectFilter filter = (ObjectFilter) filters.peek();
		if (filter.needFilter() && filter instanceof PaymentPointFilter) {
			PaymentPointFilter paymentPointFilter = (PaymentPointFilter) filter;
			return cashboxDao.findCashboxesForPaymentPoint(paymentPointFilter.getSelectedId());
		}

		return cashboxDao.findCashboxes(new Page<Cashbox>(10000, 1));
	}

	/**
	 * List available cashboxes
	 *
	 * @param filters Filters stack
	 * @param pager   Pager
	 * @return List of available cashboxes
	 */
	@NotNull
	@Override
	public List<Cashbox> listCashboxes(@NotNull ArrayStack filters, @NotNull Page<Cashbox> pager) {
		if (filters.isEmpty()) {
			return cashboxDao.findCashboxes(pager);
		}

		// check if payments collector filter is there
		ObjectFilter filter = (ObjectFilter) filters.peek();
		if (filter.needFilter() && filter instanceof PaymentPointFilter) {
			PaymentPointFilter paymentPointFilter = (PaymentPointFilter) filter;
			return cashboxDao.listCashboxes(paymentPointFilter.getSelectedId(), pager);
		}

		return cashboxDao.findCashboxes(pager);
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<Cashbox> findObjects(Page<Cashbox> pager) {
		return cashboxDao.findCashboxes(pager);
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<Cashbox> findCashboxesForPaymentPoint(Long paymentPointId) {
		return cashboxDao.findCashboxesForPaymentPoint(paymentPointId);
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<Cashbox> findCashboxesForPaymentCollector(@NotNull Stub<PaymentCollector> paymentCollectorStub, Page<Cashbox> pager) {
		return cashboxDao.findCashboxesForPaymentCollector(paymentCollectorStub.getId(), pager);
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
