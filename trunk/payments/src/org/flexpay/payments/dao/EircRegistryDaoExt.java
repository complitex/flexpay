package org.flexpay.payments.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryDaoExt;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EircRegistryDaoExt {

	/**
	 * Find registries
	 *
	 * @param filters ObjectFilters
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	List<Registry> findRegistries(Collection<ObjectFilter> filters, Page<?> pager);

	void deleteQuittances(Long registryId);
}
