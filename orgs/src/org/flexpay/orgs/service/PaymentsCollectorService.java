package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.OrganizationInstanceFilter;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface PaymentsCollectorService
		extends OrganizationInstanceService<PaymentsCollectorDescription, PaymentsCollector> {

	/**
	 * List registered instances
	 *
	 * @param pager Page
	 * @return List of registered instances
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_READ)
	@NotNull
	List<PaymentsCollector> listInstances(@NotNull Page<PaymentsCollector> pager);

	/**
	 * Disable instances
	 *
	 * @param objectIds Instances identifiers to disable
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full instance info
	 *
	 * @param stub Instance stub
	 * @return Instance if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_READ)
	@Nullable
	<T extends PaymentsCollector>
	T read(@NotNull Stub<T> stub);

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return saved instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_ADD)
	@NotNull
	PaymentsCollector create(@NotNull PaymentsCollector instance) throws FlexPayExceptionContainer;

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return updated instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_CHANGE)
	@NotNull
	PaymentsCollector update(@NotNull PaymentsCollector instance) throws FlexPayExceptionContainer;

	/**
	 * Initialize organizations filter, includes only organizations that are not instances of type <code>T</code> or this
	 * particular <code>instance</code> organization
	 *
	 * @param filter   Filter to initialize
	 * @param instance Organisation Instance
	 * @return filter
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_READ)
	@NotNull
	OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull PaymentsCollector instance);

	/**
	 * Initialize instances filter
	 *
	 * @param filter Instance filter to init
	 * @return Filter back
	 */
	@Secured (Roles.PAYMENTS_COLLECTOR_READ)
	@NotNull
	PaymentsCollectorFilter initFilter(@NotNull PaymentsCollectorFilter filter);
}
