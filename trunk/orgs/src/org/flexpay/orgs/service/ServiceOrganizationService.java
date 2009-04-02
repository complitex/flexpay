package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceOrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

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
	List<ServiceOrganization> listServiceOrganizations(Page<? extends ServiceOrganization> pager);

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
