package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;

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
	 * Find service providers
	 *
	 * @param range    Range
        * @return list o organizations
	 */
	List<ServiceProvider> listInstancesWithIdentities(FetchRange range);

	/**
	 * Get a list of organizations that do not have active service providers
	 *
	 * @return list of organizations
	 */
	List<Organization> findProviderlessOrgs();

}
