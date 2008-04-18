package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.common.dao.paging.Page;

import java.util.Date;
import java.util.List;

public interface SpRegistryDaoExt {

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
}
