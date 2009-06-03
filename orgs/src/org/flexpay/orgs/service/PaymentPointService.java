package org.flexpay.orgs.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface PaymentPointService {

	/**
	 * List available payment points
	 *
	 * @param filters Filters stack
	 * @param pager   Pager
	 * @return List of available Payment points
	 */
	@NotNull
	List<PaymentPoint> listPoints(@NotNull ArrayStack filters, @NotNull Page<PaymentPoint> pager);

	/**
	 * Read full payment point info
	 *
	 * @param stub payment point stub
	 * @return Payment point if available, or <code>null</code> if not found
	 */
	@Nullable
	PaymentPoint read(@NotNull Stub<PaymentPoint> stub);

	/**
	 * Disable payment points
	 *
	 * @param objectIds Payment points identifiers to disable
	 */
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Create a new PaymentPoint
	 *
	 * @param point Point to persist
	 * @return Persisted point
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	PaymentPoint create(@NotNull PaymentPoint point) throws FlexPayExceptionContainer;

	/**
	 * Update PaymentPoint
	 *
	 * @param point Point to update
	 * @return PaymentPoint back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	PaymentPoint update(@NotNull PaymentPoint point) throws FlexPayExceptionContainer;

	/**
	 * Initialize payment points filter
	 *
	 * @param filter PaymentPointsFilter to initialize
	 * @return filter back
	 */
	@NotNull
	PaymentPointsFilter initFilter(@NotNull PaymentPointsFilter filter);


	List<PaymentPoint> findAll();

}
