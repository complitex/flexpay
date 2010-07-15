package org.flexpay.payments.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.sorter.RegistrySorter;

import java.util.Collection;
import java.util.List;

public interface EircRegistryDaoExt {

	/**
	 * Find registries
	 *
     * @param registrySorter registry sorter
	 * @param filters ObjectFilters
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	List<Registry> findRegistries(RegistrySorter registrySorter, Collection<ObjectFilter> filters, Page<?> pager);

	void deleteQuittances(Long registryId);
}
