package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceOrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface ServiceOrganizationService
		extends OrganizationInstanceService<ServiceOrganizationDescription, ServiceOrganization> {

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
	@NotNull
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	List<ServiceOrganization> listInstances(@NotNull Page<ServiceOrganization> pager);

	/**
	 * Read ServiceOrganization object by its unique id
	 *
	 * @param stub ServiceOrganization stub
	 * @return ServiceOrganization object, or <code>null</code> if object not found
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	<T extends ServiceOrganization>
	T read(@NotNull Stub<T> stub);

	/**
	 * Disable service organizations
	 *
	 * @param objectIds Service organizations identifiers to disable
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Create service organization
	 *
	 * @param serviceOrganization Service organization to save
	 * @return persisted instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_ADD)
	@NotNull
	ServiceOrganization create(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer;

	/**
	 * Update service organization
	 *
	 * @param serviceOrganization Service organization to save
	 * @return updated instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_CHANGE)
	@NotNull
	ServiceOrganization update(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer;

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
	@NotNull
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull ServiceOrganization serviceOrganization);

	void delete(@NotNull ServiceOrganization org);
}
