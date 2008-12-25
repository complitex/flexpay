package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.OrganizationInstance;
import org.flexpay.eirc.persistence.OrganizationInstanceDescription;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface OrganisationInstanceService<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> {

	/**
	 * List registered instances
	 *
	 * @param pager Page
	 * @return List of registered instances
	 */
	@NotNull
	List<T> listInstances(@NotNull Page<T> pager);

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
	T read(@NotNull Stub<T> stub);

	/**
	 * Create instance
	 *
	 * @param instance Organisation instance to save
	 * @return saved instance back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	T create(@NotNull T instance) throws FlexPayExceptionContainer;

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return updated instance back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	T update(@NotNull T instance) throws FlexPayExceptionContainer;

	/**
	 * Initialize organizations filter, includes only organizations that are not instances of type <code>T</code> or this
	 * particular <code>instance</code> organization
	 *
	 * @param filter   Filter to initialize
	 * @param instance Organisation Instance
	 * @return filter
	 */
	@NotNull
	OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull T instance);
}
