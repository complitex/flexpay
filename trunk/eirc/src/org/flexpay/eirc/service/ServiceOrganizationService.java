package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.ServiceOrganizationFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface ServiceOrganizationService {

	/**
	 * Get a list of available ServiceOrganizations
	 *
	 * @return List of ServiceOrganizations
	 */
	@NotNull
	List<ServiceOrganization> listServiceOrganizations();

    /**
     * List service organizations
     *
     * @param pager Page
     * @return List of service organizations
     */
    List<ServiceOrganization> listServiceOrganizations(Page<ServiceOrganization> pager);

    /**
     * Read full service organization info
     *
     * @param serviceOrganization Service organization
     * @return ServiceOrganization
     */
    ServiceOrganization read(ServiceOrganization serviceOrganization);

	/**
	 * Read ServiceOrganization object by its unique id
	 *
	 * @param stub ServiceOrganization stub
	 * @return ServiceOrganization object, or <code>null</code> if object not found
	 */
	ServiceOrganization read(@NotNull Stub<ServiceOrganization> stub);

    /**
     * Disable service organizations
     *
     * @param objectIds Service organizations identifiers to disable
     */
    void disable(@NotNull Set<Long> objectIds);

    /**
     * Save or update service organization
     *
     * @param serviceOrganization Service organization to save
     * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
     */
    void save(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer;

	/**
	 * Get served buildings
	 * 
	 * @param stub ServiceOrganization stub
	 * @return set of served organizations
	 */
	Set<ServedBuilding> findServedBuildings(@NotNull Stub<ServiceOrganization> stub);

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceOrganizationFilter to initialize
	 * @return ServiceOrganizationFilter back
	 */
	ServiceOrganizationFilter initServiceOrganizationsFilter(ServiceOrganizationFilter filter);

    /**
     * Initialize organizations filter, includes only organizations that are not banks or this particular <code>service organization</code>
     * organization
     *
     * @param organizationFilter Filter to initialize
     * @param serviceOrganization service organization
     */
    void initServiceOrganizationlessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull ServiceOrganization serviceOrganization);

}
