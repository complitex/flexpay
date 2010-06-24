package org.flexpay.orgs.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.dao.PaymentPointDao;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPointName;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Payment points service implementation
 */
@Transactional (readOnly = true)
public class PaymentPointServiceImpl implements PaymentPointService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SessionUtils sessionUtils;
	private ModificationListener<PaymentPoint> modificationListener;

	protected PaymentPointDao paymentPointDao;

	/**
	 * List available payment points
	 *
	 * @param filters Filters stack
	 * @param pager   Pager
	 * @return List of available Payment points
	 */
	@NotNull
	public List<PaymentPoint> listPoints(@NotNull ArrayStack filters, @NotNull Page<PaymentPoint> pager) {

		if (filters.isEmpty()) {
			return paymentPointDao.listPoints(pager);
		}

		// check if payments collector filter is there
		ObjectFilter filter = (ObjectFilter) filters.peek();
		if (filter.needFilter() && filter instanceof PaymentCollectorFilter) {
			PaymentCollectorFilter collectorFilter = (PaymentCollectorFilter) filter;
			return paymentPointDao.listCollectorPoints(collectorFilter.getSelectedId(), pager);
		}

		return paymentPointDao.listPoints(pager);
	}

	/**
	 * List available payment points if payments collector is selected in filters stack, empty list otherwise
	 *
	 * @param filters Filters stack
	 * @param pager   Pager
	 * @return available payment points if payments collector is selected in filters stack, empty list otherwise
	 */
	@NotNull
	public List<PaymentPoint> listCollectorPoints(@NotNull ArrayStack filters, @NotNull Page<PaymentPoint> pager) {
		
		// check if payments collector filter is there
		ObjectFilter filter = (ObjectFilter) filters.peek();
		if (filter.needFilter() && filter instanceof PaymentCollectorFilter) {
			PaymentCollectorFilter collectorFilter = (PaymentCollectorFilter) filter;
			return paymentPointDao.listCollectorPoints(collectorFilter.getSelectedId(), pager);
		}

		return CollectionUtils.list();
	}

    /**
     * Payment point`s tradingDayProcessInstanceId is not null.
     *
     * @return list payment points
     */
    @NotNull
    public List<PaymentPoint> listPaymentPointsWithTradingDay() {
        return paymentPointDao.listPaymentPointsWithTradingDay();
    }

    /**
     * Payment point`s tradingDayProcessInstanceId is null.
     *
     * @return list payment points
     */
    @NotNull
    public List<PaymentPoint> listPaymentPointsWithoutTradingDay() {
        return paymentPointDao.listPaymentPointsWithoutTradingDay();
    }

	/**
	 * Read full payment point info
	 *
	 * @param stub payment point stub
	 * @return Payment point if available, or <code>null</code> if not found
	 */
	@Nullable
	public PaymentPoint read(@NotNull Stub<PaymentPoint> stub) {
		return paymentPointDao.readFull(stub.getId());
	}

    @Override
    public PaymentPoint find(@NotNull Stub<PaymentPoint> stubPoint, @NotNull Stub<PaymentCollector> stubCollector) {
        List<PaymentPoint> paymentPoints = paymentPointDao.findByIdAndCollectorId(stubPoint.getId(), stubCollector.getId());
        return paymentPoints == null || paymentPoints.isEmpty() ? null : paymentPoints.get(0);
    }

    /**
	 * Read full payment points info
	 *
	 * @param ids		   payment point identifiers
	 * @param preserveOrder Whether to preserve result order
	 * @return Payment points found
	 */
	@NotNull
	@Override
	public List<PaymentPoint> readFull(Collection<Long> ids, boolean preserveOrder) {
		return paymentPointDao.readFullCollection(ids, preserveOrder);
	}

	/**
	 * Disable payment points
	 *
	 * @param objectIds Payment points identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {

		for (Long id : objectIds) {
			PaymentPoint point = paymentPointDao.read(id);
			if (point != null) {
				point.disable();
				paymentPointDao.update(point);

				modificationListener.onDelete(point);
				log.info("Disabled: {}", point);
			}
		}
	}

	/**
	 * Create a new PaymentPoint
	 *
	 * @param point Point to persist
	 * @return Persisted point
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	public PaymentPoint create(@NotNull PaymentPoint point) throws FlexPayExceptionContainer {
		validate(point);

		if (point.isNotNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("Not new", "common.error.create_saved"));
		}

		// id=0 is also a new object
		point.setId(null);
		paymentPointDao.create(point);

		modificationListener.onCreate(point);

		return point;
	}

	/**
	 * Updaet PaymentPoint
	 *
	 * @param point Point to update
	 * @return PaymentPoint back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	public PaymentPoint update(@NotNull PaymentPoint point) throws FlexPayExceptionContainer {
		validate(point);

		if (point.isNew()) {
			throw new FlexPayExceptionContainer(new FlexPayException("New", "common.error.update_new"));
		}

		PaymentPoint old = read(stub(point));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + point));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, point);

		paymentPointDao.update(point);

		return point;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(PaymentPoint point) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		PaymentCollector collector = point.getCollector();
		if (collector == null || collector.isNew()) {
			ex.addException(new FlexPayException("Invalid collector", "eirc.error.payment_point.no_collector"));
		}

		if (StringUtils.isBlank(point.getAddress())) {
			ex.addException(new FlexPayException("No address", "eirc.error.payment_point.no_address"));
		}

		boolean defaultNameFound = false;
		for (PaymentPointName translation : point.getNames()) {
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
			throw ex;
		}
	}

	/**
	 * Initialize payment points filter
	 *
	 * @param filter PaymentPointsFilter to initialize
	 * @return filter back
	 */
	@NotNull
	public PaymentPointsFilter initFilter(@NotNull PaymentPointsFilter filter) {

		return initFilter(CollectionUtils.arrayStack(), filter);
	}

	/**
	 * Initialize payment points filter
	 *
	 * @param filters filters stack
	 * @param filter PaymentPointsFilter to initialize
	 * @return filter back
	 */
	@NotNull
	public PaymentPointsFilter initFilter(@NotNull ArrayStack filters, @NotNull PaymentPointsFilter filter) {
		
		List<PaymentPoint> points = listPoints(filters, new Page<PaymentPoint>(10000, 1));
		filter.setPoints(points);
		return filter;
	}

	public List<PaymentPoint> findAll() {
		return paymentPointDao.listPoints();
	}

	@Override
	@Transactional (readOnly = false)
	public void delete(@NotNull PaymentPoint point) {
		paymentPointDao.delete(point);
	}

	@Required
	public void setPaymentPointDao(PaymentPointDao paymentPointDao) {
		this.paymentPointDao = paymentPointDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<PaymentPoint> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
