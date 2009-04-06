package org.flexpay.orgs.service.imp;

import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.dao.ServiceProviderDao;
import org.flexpay.orgs.dao.ServiceProviderDaoExt;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Transactional (readOnly = true)
public class ServiceProviderServiceImpl implements ServiceProviderService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceProviderDao serviceProviderDao;
	private ServiceProviderDaoExt serviceProviderDaoExt;
	private DataSourceDescriptionDao dataSourceDescriptionDao;

	/**
	 * Read full service provider info
	 *
	 * @param stub provider stub
	 * @return ServiceProvider
	 */
	public ServiceProvider read(Stub<ServiceProvider> stub) {
		return serviceProviderDao.readFull(stub.getId());
	}

	/**
	 * Find service provider by its number
	 *
	 * @param providerNumber Service provider unique number
	 * @return ServiceProvider instance
	 * @throws IllegalArgumentException if provider cannot be found
	 */
	public ServiceProvider getProvider(Long providerNumber) throws IllegalArgumentException {
		ServiceProvider serviceProvider = serviceProviderDaoExt.findByNumber(providerNumber);
		if (serviceProvider == null) {
			throw new IllegalArgumentException("Cannot find service provider with number #" + providerNumber);
		}

		return serviceProvider;
	}

	/**
	 * List service providers
	 *
	 * @param pager Page
	 * @return List of service providers
	 */
	public List<ServiceProvider> listProviders(Page<ServiceProvider> pager) {
		return serviceProviderDao.findProviders(pager);
	}

	/**
	 * Disable service providers
	 *
	 * @param objectIds Set of service provider identifiers
	 */
	@Transactional (readOnly = false)
	public void disable(Set<Long> objectIds) {
		for (Long id : objectIds) {
			ServiceProvider provider = serviceProviderDao.read(id);
			if (provider != null) {
				provider.disable();
				serviceProviderDao.update(provider);
			}
		}
	}

	/**
	 * Save service provider
	 *
	 * @param serviceProvider New or persitent object to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if provider validation fails
	 */
	@Transactional (readOnly = false)
	public void save(ServiceProvider serviceProvider) throws FlexPayExceptionContainer {
		validate(serviceProvider);
		if (serviceProvider.isNew()) {
			serviceProvider.setId(null);

			// create data source description with provider default description text
			DataSourceDescription sd = new DataSourceDescription();
			sd.setDescription(serviceProvider.getDefaultDescription());
			dataSourceDescriptionDao.create(sd);
			serviceProvider.setDataSourceDescription(sd);

			serviceProviderDao.create(serviceProvider);
		} else {
			serviceProviderDao.update(serviceProvider);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(ServiceProvider sp) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		log.info("Provider organization: {}", sp.getOrganization());

		if (sp.getOrganization() == null || sp.getOrganization().isNew()) {
			container.addException(new FlexPayException(
					"No organization selected", "eirc.error.service_provider.no_organization_specified"));
		}
		// todo validate organization id was not changed for existing provider

		boolean defaultDescFound = false;
		for (ServiceProviderDescription description : sp.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.service_provider.no_default_lang_description"));
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	/**
	 * Initialize filter with organizations that do not have active service providers
	 * <p/>
	 * todo: implement in a more efficient way
	 *
	 * @param organizationFilter filter to init
	 * @param sp				 Service Provider
	 * @return filter
	 */
	public OrganizationFilter initOrganizationFilter(OrganizationFilter organizationFilter, ServiceProvider sp) {
		List<Organization> organizations = serviceProviderDao.findProviderlessOrgs();
		List<Organization> providerlessOrgs = new ArrayList<Organization>();
		Long orgId = sp.getOrganization() != null ? sp.getOrganizationStub().getId() : null;
		OUTER:
		for (Organization org : organizations) {
			//noinspection ConstantConditions
			if (org.getId().equals(orgId)) {
				providerlessOrgs.add(org);
				continue;
			}
			for (ServiceProvider provider : org.getServiceProviders()) {
				if (provider.isActive()) {
					continue OUTER;
				}
			}
			providerlessOrgs.add(org);
		}

		organizationFilter.setOrganizations(providerlessOrgs);
		if (orgId != null && !orgId.equals(0L)) {
			organizationFilter.setReadOnly(true);
			// todo ensure organization really exists
			organizationFilter.setSelectedId(orgId);
		}

		return organizationFilter;
	}

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceProviderFilter to initialize
	 * @return ServiceProviderFilter back
	 */
	public ServiceProviderFilter initServiceProvidersFilter(ServiceProviderFilter filter) {

		if (filter == null) {
			filter = new ServiceProviderFilter();
		}

		List<ServiceProvider> providers = serviceProviderDao.findProviders(new Page<ServiceProvider>(10000, 1));
		filter.setInstances(providers);

		return filter;
	}

	@Required
	public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
		this.serviceProviderDao = serviceProviderDao;
	}

	@Required
	public void setServiceProviderDaoExt(ServiceProviderDaoExt serviceProviderDaoExt) {
		this.serviceProviderDaoExt = serviceProviderDaoExt;
	}

	@Required
	public void setDataSourceDescriptionDao(DataSourceDescriptionDao dataSourceDescriptionDao) {
		this.dataSourceDescriptionDao = dataSourceDescriptionDao;
	}
}
