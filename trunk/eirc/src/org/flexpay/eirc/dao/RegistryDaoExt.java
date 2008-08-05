package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Collection;

public interface RegistryDaoExt {

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
	List<SpRegistry> findRegistries(OrganisationFilter senderFilter, OrganisationFilter recipientFilter,
									RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager);

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	Collection<SpRegistry> findRegistries(@NotNull Set<Long> objectIds);

	/**
	 * Check if registry has more records to process
	 *
	 * @param registryId Registry id
	 * @return <code>true</code> if registry has records for processing, or <code>false</code> otherwise
	 */
	boolean hasMoreRecordsToProcess(Long registryId);
}
