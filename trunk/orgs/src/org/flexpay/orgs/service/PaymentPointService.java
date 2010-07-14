package org.flexpay.orgs.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
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
	@Secured(Roles.PAYMENT_POINT_READ)
	@NotNull
	List<PaymentPoint> listPoints(@NotNull ArrayStack filters, @NotNull Page<PaymentPoint> pager);

	/**
	 * List available payment points if payments collector is selected in filters stack, empty list otherwise
	 *
	 * @param filters Filters stack
	 * @param pager   Pager
	 * @return available payment points if payments collector is selected in filters stack, empty list otherwise
	 */
	@NotNull
	List<PaymentPoint> listCollectorPoints(@NotNull ArrayStack filters, @NotNull Page<PaymentPoint> pager);

     /**
     * Payment point`s tradingDayProcessInstanceId is not null.
     *
     * @return list payment points
     */
    @NotNull
    List<PaymentPoint> listPaymentPointsWithTradingDay();

    /**
     * Payment point`s tradingDayProcessInstanceId is null.
     *
     * @return list payment points
     */
    @NotNull
    List<PaymentPoint> listPaymentPointsWithoutTradingDay();

	/**
	 * Read full payment point info
	 *
	 * @param stub payment point stub
	 * @return Payment point if available, or <code>null</code> if not found
	 */
	@Secured(Roles.PAYMENT_POINT_READ)
	@Nullable
	PaymentPoint read(@NotNull Stub<PaymentPoint> stub);

    /**
     * Read full payment point info
     *
     * @param stubPoint payment point stub
     * @param stubCollector payment collector stub
     * @return Payment point if available, or <code>null</code> if not found
     */
    @Secured(Roles.PAYMENT_POINT_READ)
    @Nullable
    PaymentPoint find(@NotNull Stub<PaymentPoint> stubPoint, @NotNull Stub<PaymentCollector> stubCollector);

	/**
	 * Read full payment points info
	 *
	 * @param ids payment point identifiers
	 * @param preserveOrder Whether to preserve result order
	 * @return Payment points found
	 */
	@Secured(Roles.PAYMENT_POINT_READ)
	@NotNull
	List<PaymentPoint> readFull(Collection<Long> ids, boolean preserveOrder);

	/**
	 * Disable payment points
	 *
	 * @param objectIds Payment points identifiers to disable
	 */
	@Secured(Roles.PAYMENT_POINT_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Create a new PaymentPoint
	 *
	 * @param point Point to persist
	 * @return Persisted point
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured(Roles.PAYMENT_POINT_ADD)
	@NotNull
	PaymentPoint create(@NotNull PaymentPoint point) throws FlexPayExceptionContainer;

	/**
	 * Update PaymentPoint
	 *
	 * @param point Point to update
	 * @return PaymentPoint back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured(Roles.PAYMENT_POINT_CHANGE)
	@NotNull
	PaymentPoint update(@NotNull PaymentPoint point) throws FlexPayExceptionContainer;

	/**
	 * Initialize payment points filter
	 *
	 * @param filter PaymentPointFilter to initialize
	 * @return filter back
	 */
	@Secured(Roles.PAYMENT_POINT_READ)
	@NotNull
    PaymentPointFilter initFilter(@NotNull PaymentPointFilter filter);

	/**
	 * Initialize payment points filter
	 *
	 * @param filters filters stack
	 * @param filter PaymentPointFilter to initialize
	 * @return filter back
	 */
	@Secured(Roles.PAYMENT_POINT_READ)
	@NotNull
    PaymentPointFilter initFilter(@NotNull ArrayStack filters, @NotNull PaymentPointFilter filter);

	@Secured(Roles.PAYMENT_POINT_READ)
	List<PaymentPoint> findAll();

	void delete(@NotNull PaymentPoint point);

}
