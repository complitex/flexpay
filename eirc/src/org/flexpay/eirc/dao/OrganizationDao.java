package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Organization;

import java.util.List;

public interface OrganizationDao extends GenericDao<Organization, Long> {

	/**
	 * Find organization by id
	 *
	 * @param id Organization id
	 * @return List of organizations
	 */
	List<Organization> findOrganizationsById(String id);

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

}
