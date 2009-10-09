package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;

import java.util.List;

public interface OrganizationDao extends GenericDao<Organization, Long> {

	/**
	 * get all organizations
	 *
	 * @return List of organizations
	 */
	List<Organization> findAllOrganizations();

	/**
	 * Find organizations
	 *
	 * @param pager Page
	 * @return list o organizations
	 */
	List<Organization> findOrganizations(Page<Organization> pager);

	/**
	 * Find organizations which have any {@link org.flexpay.orgs.persistence.PaymentCollector}s
	 * @return list of organizations
	 */
	List<Organization> findOrganizationsWithCollectors();

}
