package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface PaymentsCollectorService
		extends OrganisationInstanceService<PaymentsCollectorDescription, PaymentsCollector> {

	/**
	 * List registered instances
	 *
	 * @param pager Page
	 * @return List of registered instances
	 */
	@NotNull
	List<PaymentsCollector> listInstances(@NotNull Page<PaymentsCollector> pager);

	/**
	 * Disable instances
	 *
	 * @param objectIds Instances identifiers to disable
	 */
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full instance info
	 *
	 * @param stub Instance stub
	 * @return Instance if found, or <code>null</code> otherwise
	 */
	@Nullable
	PaymentsCollector read(@NotNull Stub<PaymentsCollector> stub);

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return saved instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
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
	@NotNull
	OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull PaymentsCollector instance);
}
