package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface PaymentCollectorService
		extends OrganizationInstanceService<PaymentCollectorDescription, PaymentCollector> {

	/**
	 * List registered instances
	 *
	 * @param pager Page
	 * @return List of registered instances
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_READ)
	@NotNull
	List<PaymentCollector> listInstances(@NotNull Page<PaymentCollector> pager);

    /**
	 * List registered instances
	 *
	 * @range Range
	 * @return List of registered instances
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_READ)
	@NotNull
	List<PaymentCollector> listInstances(@NotNull FetchRange range);

	/**
	 * Disable instances
	 *
	 * @param objectIds Instances identifiers to disable
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full instance info
	 *
	 * @param stub Instance stub
	 * @return Instance if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_READ)
	@Nullable
	<T extends PaymentCollector>
	T read(@NotNull Stub<T> stub);

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return saved instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_ADD)
	@NotNull
	PaymentCollector create(@NotNull PaymentCollector instance) throws FlexPayExceptionContainer;

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return updated instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_CHANGE)
	@NotNull
	PaymentCollector update(@NotNull PaymentCollector instance) throws FlexPayExceptionContainer;

	/**
	 * Initialize organizations filter, includes only organizations that are not instances of type <code>T</code> or this
	 * particular <code>instance</code> organization
	 *
	 * @param filter   Filter to initialize
	 * @param instance Organisation Instance
	 * @return filter
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_READ)
	@NotNull
	OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull PaymentCollector instance);

	/**
	 * Initialize instances filter
	 *
	 * @param filter Instance filter to init
	 * @return Filter back
	 */
	@Secured (Roles.PAYMENT_COLLECTOR_READ)
	@NotNull
	PaymentCollectorFilter initFilter(@NotNull PaymentCollectorFilter filter);
}
