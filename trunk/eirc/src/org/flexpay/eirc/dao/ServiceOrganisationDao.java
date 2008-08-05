package org.flexpay.eirc.dao;

import java.util.List;
import java.util.Set;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;

public interface ServiceOrganisationDao extends GenericDao<ServiceOrganisation, Long> {

	/**
	 * Find organisation by id
	 *
	 * @return List if organisations
	 */
	List<ServiceOrganisation> listServiceOrganisation();
	
	/**
	 * Find ServedBuildings by ServiceOrganisation key
	 *
	 * @param serviceOrganisationId ServiceOrganisation key
	 * @return Set of ServedBuilding
	 */
	Set<ServedBuilding> findServedBuildings(Long serviceOrganisationId);
}
