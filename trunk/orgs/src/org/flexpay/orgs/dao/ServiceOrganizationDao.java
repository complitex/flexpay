package org.flexpay.orgs.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceOrganization;

import java.util.List;

public interface ServiceOrganizationDao extends GenericDao<ServiceOrganization, Long> {

    /**
     * Find service organizations
     *
     * @return List of service organizations
     */
    List<ServiceOrganization> listServiceOrganizations();

    /**
     * Find service organizations
     *
     * @param pager Page
     * @return list of service organizations
     */
    List<ServiceOrganization> findServiceOrganizations(Page<? extends ServiceOrganization> pager);

    /**
     * Find service organizations for organization
     *
     * @param organizationId Organization key
     * @return List of service organizations for organization
     */
    List<ServiceOrganization> findOrganizationServiceOrganizations(Long organizationId);

    /**
     * Find organizations that are not service organizations except of that has a service organization
     * with specified <code>includedServiceOrganizationId</code>
     *
     * @param includedServiceOrganizationId Allowed service organization key, that organization will also be in a resulting list
     * @return List of organizations that are not service organizations
     */
    List<Organization> findServiceOrganizationlessOrganizations(Long includedServiceOrganizationId);
}
