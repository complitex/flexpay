package org.flexpay.eirc.dao;

import org.flexpay.ab.persistence.Buildings;
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

    /**
     * Find buildings in the street
     *
     * @param streetId Street identifier
     * @param serviceOrganizationId Service organization identifier
     * @param pager Page instance
     * @return list of buildings for the street
     */
    List<Buildings> findBuildingsWithBuildings(Long streetId, Long serviceOrganizationId, Page pager);

    /**
     * Find buildings in the street and district
     *
     * @param streetId Street identifier
     * @param districtId District identifier
     * @param serviceOrganizationId Service organization identifier
     * @param pager Page instance
     * @return list of buildings for the street
     */
    List<Buildings> findStreetDistrictBuildingsWithBuildings(Long streetId, Long districtId, Long serviceOrganizationId, Page pager);

    List<ServedBuilding> findServedBuildings(Collection<Long> ids);

}
