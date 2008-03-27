package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Organisation;

import java.util.List;

public interface OrganisationDao extends GenericDao<Organisation, Long> {

	/**
	 * Find organisation by id
	 *
	 * @param id Organisation id
	 * @return List if organisations
	 */
	List<Organisation> findOrganisationsById(String id);
}
