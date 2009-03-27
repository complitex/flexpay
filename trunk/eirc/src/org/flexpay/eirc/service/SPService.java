package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.ServiceFilter;
import org.flexpay.eirc.persistence.filters.ServiceProviderFilter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

/**
 * Service providers helper service
 */
public interface SPService {

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
	@Secured (Roles.SERVICE_PROVIDER_READ)
	List<ServiceProvider> listProviders(Page<ServiceProvider> pager);

	/**
	 * Disable service providers
	 *
	 * @param objectIds Set of service provider identifiers
	 */
	@Secured (Roles.SERVICE_PROVIDER_DELETE)
	void disable(Set<Long> objectIds);

	/**
	 * Read full service provider info
	 *
	 * @param provider Service Provider stub
	 * @return ServiceProvider
	 */
	@Secured (Roles.SERVICE_PROVIDER_READ)
	ServiceProvider read(ServiceProvider provider);

	/**
	 * Save service provider
	 *
	 * @param serviceProvider New or persitent object to save
	 * @throws FlexPayExceptionContainer if provider validation fails
	 */
	@Secured ({Roles.SERVICE_PROVIDER_ADD, Roles.SERVICE_PROVIDER_CHANGE})
	void save(ServiceProvider serviceProvider) throws FlexPayExceptionContainer;

	/**
	 * Initialize filter with organizations that do not have active service providers
	 *
	 * @param organizationFilter filter to init
	 * @param sp				 ServiceProvider to init filter for
	 * @return filter
	 */
	@Secured (Roles.SERVICE_ORGANIZATION_READ)
	OrganizationFilter initOrganizationFilter(OrganizationFilter organizationFilter, ServiceProvider sp);

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceProviderFilter to initialize
	 * @return ServiceProviderFilter back
	 */
	@Secured (Roles.SERVICE_PROVIDER_READ)
	ServiceProviderFilter initServiceProvidersFilter(ServiceProviderFilter filter);

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	@Secured (Roles.SERVICE_READ)
	List<Service> listServices(List<ObjectFilter> filters, Page<Service> pager);

	/**
	 * Read full service information
	 *
	 * @param stub Service stub
	 * @return Service description
	 */
	@Secured (Roles.SERVICE_READ)
	@Nullable
	Service read(@NotNull Stub<Service> stub);

	/**
	 * Create or update service
	 *
	 * @param service Service to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.SERVICE_ADD, Roles.SERVICE_CHANGE})
	void save(Service service) throws FlexPayExceptionContainer;

	/**
	 * Initalize service filter with a list of parent services
	 *
	 * @param filter Filter to initialize
	 * @return Filter back
	 */
	@Secured (Roles.SERVICE_READ)
	ServiceFilter initParentServicesFilter(ServiceFilter filter);
}
