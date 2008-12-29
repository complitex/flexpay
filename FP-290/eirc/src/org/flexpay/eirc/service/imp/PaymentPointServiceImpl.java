package org.flexpay.eirc.service.imp;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.PaymentPointDao;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.eirc.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.ArrayStack;

import java.util.List;
import java.util.Set;

/**
 * Payment points service implementation
 */
@Transactional (readOnly = true)
public class PaymentPointServiceImpl implements PaymentPointService {

	private PaymentPointDao paymentPointDao;

	/**
	 * List available payment paints
	 *
	 * @param townStub Town stub to lookup points in
	 * @param pager	Pager
	 * @return List of available Payment points
	 */
	@NotNull
	public List<PaymentPoint> listPoints(@NotNull Stub<Town> townStub, @NotNull Page<PaymentPoint> pager) {
		return paymentPointDao.listPoints(pager);
	}

	/**
	 * List available payment paints
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

	public void setPaymentPointDao(PaymentPointDao paymentPointDao) {
		this.paymentPointDao = paymentPointDao;
	}
}
