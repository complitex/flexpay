package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RegistryService {

	/**
	 * Create SpRegistry
	 *
	 * @param registry SpRegistry
	 * @return created SpRegistry object
	 * @throws FlexPayException if failure occurs
	 */
	public Registry create(Registry registry) throws FlexPayException;

	/**
	 * Get all SpRegistry by spFile id in page mode
	 *
	 * @param pager	Page object
	 * @param spFileId spFile id
	 * @return List of SpRegistry objects for pager
	 */
	List<Registry> findObjects(Page<Registry> pager, Long spFileId);

	/**
	 * Read SpRegistry object by its unique id
	 *
	 * @param stub Registry stub
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	@Nullable
	Registry read(@NotNull Stub<Registry> stub);

	/**
	 * Read Registry with containers included
	 *
	 * @param stub Registry stub
	 * @return Registry if found, or <code>null</code> otherwise
	 */
	@Nullable
	Registry readWithContainers(@NotNull Stub<Registry> stub);

	/**
	 * Update SpRegistry
	 *
	 * @param registry SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws FlexPayException if SpRegistry object is invalid
	 */
	Registry update(Registry registry) throws FlexPayException;

	void delete(Registry registry);

	/**
	 * Find registries
	 *
	 * @param senderFilter	sender organization filter
	 * @param recipientFilter recipient organization filter
	 * @param typeFilter	  registry type filter
	 * @param fromDate		registry generation start date
	 * @param tillDate		registry generation end date
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	List<Registry> findObjects(OrganizationFilter senderFilter, OrganizationFilter recipientFilter,
								 RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager);

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	Collection<Registry> findObjects(@NotNull Set<Long> objectIds);

	/**
	 * Find registry recieved from specified sender with a specified number
	 *
	 * @param registryNumber Registry number to search for
	 * @param senderStub	 Sender organization stub
	 * @return Registry reference if found, or <code>null</code> otherwise
	 */
	@Nullable
	Registry getRegistryByNumber(@NotNull Long registryNumber, @NotNull Stub<Organization> senderStub);
}
