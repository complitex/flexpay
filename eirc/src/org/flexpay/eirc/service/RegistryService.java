package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Collection;

public interface RegistryService {

	/**
	 * Create SpRegistry
	 *
	 * @param spRegistry SpRegistry
	 * @return created SpRegistry object
	 * @throws FlexPayException if failure occurs
	 */
	public SpRegistry create(SpRegistry spRegistry) throws FlexPayException;

	/**
	 * Get all SpRegistry by SpFile in page mode
	 *
	 * @param pager Page object
	 * @param spFileId SpFile key
	 * @return List of SpRegistry objects for pager
	 */
	List<SpRegistry> findObjects(Page<SpRegistry> pager, Long spFileId);

	/**
	 * Read SpRegistry object by its unique id
	 *
	 * @param stub Registry stub
	 * @return SpRegistry object, or <code>null</code> if object not found
	 */
	@Nullable
	SpRegistry read(@NotNull Stub<SpRegistry> stub);

	/**
	 * Read Registry with containers included
	 *
	 * @param stub Registry stub
	 * @return Registry if found, or <code>null</code> otherwise
	 */
	@Nullable
	SpRegistry readWithContainers(@NotNull Stub<SpRegistry> stub);

	/**
	 * Update SpRegistry
	 *
	 * @param spRegistry SpRegistry to update for
	 * @return Updated SpRegistry object
	 * @throws FlexPayException if SpRegistry object is invalid
	 */
	SpRegistry update(SpRegistry spRegistry) throws FlexPayException;

	void delete(SpRegistry spRegistry);

	/**
	 * Find registries
	 *
	 * @param senderFilter	sender organisation filter
	 * @param recipientFilter recipient organisation filter
	 * @param typeFilter	  registry type filter
	 * @param fromDate		registry generation start date
	 * @param tillDate		registry generation end date
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	List<SpRegistry> findObjects(OrganisationFilter senderFilter, OrganisationFilter recipientFilter,
								 RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager);

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	Collection<SpRegistry> findObjects(@NotNull Set<Long> objectIds);

	/**
	 * Find registry recieved from specified sender with a specified number
	 *
	 * @param registryNumber Registry number to search for
	 * @param senderStub Sender organisation stub
	 * @return Registry reference if found, or <code>null</code> otherwise
	 */
	@Nullable
	SpRegistry getRegistryByNumber(@NotNull Long registryNumber, @NotNull Stub<Organisation> senderStub);
}
