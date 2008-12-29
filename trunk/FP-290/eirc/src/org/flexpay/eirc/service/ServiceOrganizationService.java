package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.ServiceOrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ServiceOrganizationService {

	/**
	 * Get a list of available ServiceOrganizations
	 *
	 * @return List of ServiceOrganizations
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	@NotNull
	List<ServiceOrganization> listServiceOrganizations();

	/**
	 * List service organizations
	 *
	 * @param pager Page
	 * @return List of service organizations
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	List<ServiceOrganization> listServiceOrganizations(Page<ServiceOrganization> pager);

	/**
	 * Read full service organization info
	 *
	 * @param serviceOrganization Service organization
	 * @return ServiceOrganization
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	ServiceOrganization read(ServiceOrganization serviceOrganization);

	/**
	 * Read ServiceOrganization object by its unique id
	 *
	 * @param stub ServiceOrganization stub
	 * @return ServiceOrganization object, or <code>null</code> if object not found
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	ServiceOrganization read(@NotNull Stub<ServiceOrganization> stub);

	/**
	 * Disable service organizations
	 *
	 * @param objectIds Service organizations identifiers to disable
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Save or update service organization
	 *
	 * @param serviceOrganization Service organization to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured ({Roles.SERVICE_ORGANIZATION_ADD, Roles.SERVICE_ORGANIZATION_CHANGE})
	void save(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer;


	/**
	 * Get served buildings by ids
	 *
	 * @param ids Served building ids
	 * @return collection of served buildings
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS)
	List<ServedBuilding> findServedBuildings(@NotNull Collection<Long> ids);

	/**
	 * Get served buildings for service organization on specific page
	 *
	 * @param stub  Service organization stub
	 * @param pager Page
	 * @return Set of served buildings
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ_SERVED_BUILDINGS)
	List<ServedBuilding> findServedBuildings(@NotNull Stub<ServiceOrganization> stub, Page<ServedBuilding> pager);

	@Secured (Roles.SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS)
	void removeServedBuildings(@NotNull Set<Long> objectIds);

	/**
	 * Save or update served building
	 *
	 * @param servedBuilding Served building to save
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS)
	void saveServedBuilding(@NotNull ServedBuilding servedBuilding);

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceOrganizationFilter to initialize
	 * @return ServiceOrganizationFilter back
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	ServiceOrganizationFilter initServiceOrganizationsFilter(ServiceOrganizationFilter filter);

	/**
	 * Initialize organizations filter, includes only organizations that are not service organizations or this particular
	 * <code>service organization</code> organization
	 *
	 * @param organizationFilter  Filter to initialize
	 * @param serviceOrganization service organization
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	void initServiceOrganizationlessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull ServiceOrganization serviceOrganization);
}
