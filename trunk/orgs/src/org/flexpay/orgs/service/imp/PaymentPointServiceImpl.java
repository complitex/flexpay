package org.flexpay.orgs.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.dao.PaymentPointDao;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Payment points service implementation
 */
@Transactional (readOnly = true)
public class PaymentPointServiceImpl implements PaymentPointService {

	private Logger log = LoggerFactory.getLogger(getClass());

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
		if (filter.needFilter() && filter instanceof PaymentsCollectorFilter) {
			PaymentsCollectorFilter collectorFilter = (PaymentsCollectorFilter) filter;
			return paymentPointDao.listCollectorPoints(collectorFilter.getSelectedId(), pager);
		}

		return paymentPointDao.listPoints(pager);
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

		paymentPointDao.update(point);

		return point;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(PaymentPoint point) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		PaymentsCollector collector = point.getCollector();
		if (collector == null || collector.isNew()) {
			ex.addException(new FlexPayException("Invalid collector", "eirc.error.payment_point.no_collector"));
		}

		if (StringUtils.isBlank(point.getAddress())) {
			ex.addException(new FlexPayException("No address", "eirc.error.payment_point.no_address"));
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

		log.debug("Initializing filter");
		filter.setPoints(listPoints(CollectionUtils.arrayStack(), new Page<PaymentPoint>(10000, 1)));
		return filter;
	}

	@Required
	public void setPaymentPointDao(PaymentPointDao paymentPointDao) {
		this.paymentPointDao = paymentPointDao;
	}
}
