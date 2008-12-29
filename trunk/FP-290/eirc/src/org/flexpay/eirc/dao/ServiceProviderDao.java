package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.ServiceProvider;

import java.util.List;

public interface ServiceProviderDao extends GenericDao<ServiceProvider, Long> {

	/**
	 * Find service providers
	 *
	 * @param pager Page
	 * @return list o organizations
	 */
	List<ServiceProvider> findProviders(Page<ServiceProvider> pager);

	/**
	 * Get a list of organizations that do not have active service providers
	 *
	 * @return list of organizations
	 */
	List<Organization> findProviderlessOrgs();

}
