package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.ServiceProvider;

import java.util.List;

public interface ServiceProviderDao extends GenericDao<ServiceProvider, Long> {

	/**
	 * Find service providers
	 *
	 * @param pager Page
	 * @return list o organisations
	 */
	List<ServiceProvider> findProviders(Page<ServiceProvider> pager);

	/**
	 * Get a list of organisations that do not have active service providers
	 *
	 * @return list of organisations
	 */
	List<Organisation> findProviderlessOrgs();
}