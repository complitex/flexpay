package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.ServedBuilding;

import java.util.Collection;
import java.util.List;

public interface ServedBuildingDao extends GenericDao<ServedBuilding, Long> {

    /**
     * Find ServedBuildings by ServiceOrganization key and page
     *
     * @param serviceOrganizationId ServiceOrganization key
     * @param page page parameter
     * @return Set of ServedBuildings
     */
    List<ServedBuilding> findServedBuildingsByServiceOrganization(Long serviceOrganizationId, Page<ServedBuilding> page);

    List<ServedBuilding> findServedBuildings(Collection<Long> ids);

}
