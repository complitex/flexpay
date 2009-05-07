package org.flexpay.eirc.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryDaoExt;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EircRegistryDaoExt extends RegistryDaoExt {

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
	List<Registry> findRegistries(OrganizationFilter senderFilter, OrganizationFilter recipientFilter,
									RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page<?> pager);

	void deleteQuittances(Long registryId);
}
