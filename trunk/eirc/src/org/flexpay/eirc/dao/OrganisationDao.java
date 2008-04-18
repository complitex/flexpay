package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.Organisation;

public interface OrganisationDao extends GenericDao<Organisation, Long> {

	/**
	 * Find organisation by id
	 *
	 * @param id Organisation id
	 * @return List if organisations
	 */
	List<Organisation> findOrganisationsById(String id);

	/**
	 * get all organisations
	 *
	 * @return List of organisations
	 */
	List<Organisation> findAllOrganisations();
}
