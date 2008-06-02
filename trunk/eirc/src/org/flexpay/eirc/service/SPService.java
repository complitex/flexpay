package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.AccountRecordType;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
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
	 * Find service of specified <code>type</code> for provider
	 *
	 * @param provider ServiceProvider
	 * @param type	 ServiceType to find
	 * @return Service if found, or <code>null</code> if the requested service is not
	 *         available from <code>provider</code>
	 */
	Service getService(ServiceProvider provider, ServiceType type);

	/**
	 * Get record type by type id
	 *
	 * @param typeId Record type enum id
	 * @return record type
	 */
	AccountRecordType getRecordType(int typeId);

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
	 * @param sp ServiceProvider to init filter for
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
	 * @param pager Page
	 * @return List of services
	 */
	List<Service> listServices(List<ObjectFilter> filters, Page<Service> pager);
}
