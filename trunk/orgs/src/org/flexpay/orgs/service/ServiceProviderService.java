package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.springframework.security.annotation.Secured;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface ServiceProviderService
		extends OrganizationInstanceService<ServiceProviderDescription, ServiceProvider> {

	/**
	 * Find service provider by its number
	 *
	 * @param providerNumber Service provider unique number
	 * @return ServiceProvider instance
	 * @throws IllegalArgumentException if provider cannot be found
	 * @deprecated todo refactor to use Stub
	 */
	@Secured (Roles.SERVICE_PROVIDER_READ)
	ServiceProvider getProvider(Long providerNumber) throws IllegalArgumentException;

	/**
	 * List service providers
	 *
	 * @param pager Page
	 * @return List of service providers
	 */
	@NotNull
	@Secured (Roles.SERVICE_PROVIDER_READ)
	List<ServiceProvider> listInstances(Page<ServiceProvider> pager);

	/**
	 * Disable service providers
	 *
	 * @param objectIds Set of service provider identifiers
	 */
	@Secured (Roles.SERVICE_PROVIDER_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full service provider info
	 *
	 * @param stub Provider stub
	 * @return ServiceProvider
	 */
	@Secured (Roles.SERVICE_PROVIDER_READ)
	@Nullable
	ServiceProvider read(@NotNull Stub<ServiceProvider> stub);

	/**
	 * Create service provider
	 *
	 * @param serviceProvider New object to save
	 * @return Persisted object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if provider validation fails
	 */
	@NotNull
	@Secured (Roles.SERVICE_PROVIDER_CHANGE)
	ServiceProvider create(@NotNull ServiceProvider serviceProvider) throws FlexPayExceptionContainer;

	/**
	 * Update service provider
	 *
	 * @param serviceProvider object to update
	 * @return updated object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if provider validation fails
	 */
	@Secured (Roles.SERVICE_PROVIDER_CHANGE)
	@NotNull
	ServiceProvider update(@NotNull ServiceProvider serviceProvider) throws FlexPayExceptionContainer;

	/**
	 * Initialize filter with organizations that do not have active service providers
	 *
	 * @param organizationFilter filter to init
	 * @param sp				 ServiceProvider to init filter for
	 * @return filter
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	OrganizationFilter initInstancelessFilter(OrganizationFilter organizationFilter, ServiceProvider sp);

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceProviderFilter to initialize
	 * @return ServiceProviderFilter back
	 */
	@Secured (Roles.SERVICE_PROVIDER_READ)
	ServiceProviderFilter initServiceProvidersFilter(ServiceProviderFilter filter);
}
