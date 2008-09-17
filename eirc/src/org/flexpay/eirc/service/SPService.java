package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.ServiceFilter;
import org.flexpay.eirc.persistence.filters.ServiceProviderFilter;

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
	 */
	ServiceProvider getProvider(Long providerNumber) throws IllegalArgumentException;

	/**
	 * List service providers
	 *
	 * @param pager Page
	 * @return List of service providers
	 */
	List<ServiceProvider> listProviders(Page<ServiceProvider> pager);

	/**
	 * Disable service providers
	 *
	 * @param objectIds Set of service provider identifiers
	 */
	void disable(Set<Long> objectIds);

	/**
	 * Read full service provider info
	 *
	 * @param provider Service Provider stub
	 * @return ServiceProvider
	 */
	ServiceProvider read(ServiceProvider provider);

	/**
	 * Save service provider
	 *
	 * @param serviceProvider New or persitent object to save
	 * @throws FlexPayExceptionContainer if provider validation fails
	 */
	void save(ServiceProvider serviceProvider) throws FlexPayExceptionContainer;

	/**
	 * Initialize filter with organisations that do not have active servise providers
	 *
	 * @param organisationFilter filter to init
	 * @param sp				 ServiceProvider to init filter for
	 * @return filter
	 */
	OrganisationFilter initOrganisationFilter(OrganisationFilter organisationFilter, ServiceProvider sp);

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceProviderFilter to initialize
	 * @return ServiceProviderFilter back
	 */
	ServiceProviderFilter initServiceProvidersFilter(ServiceProviderFilter filter);

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	List<Service> listServices(List<ObjectFilter> filters, Page<Service> pager);

	/**
	 * Read full service information
	 *
	 * @param stub Service stub
	 * @return Service description
	 */
	Service read(Stub<Service> stub);

	/**
	 * Create or update service
	 *
	 * @param service Service to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(Service service) throws FlexPayExceptionContainer;

	/**
	 * Initalize service filter with a list of parent services
	 *
	 * @param parentServiceFilter Filter to initialize
	 * @return Filter back
	 */
	ServiceFilter initParentServicesFilter(ServiceFilter parentServiceFilter);
}
