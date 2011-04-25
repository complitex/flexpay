package org.flexpay.orgs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.dao.ServiceProviderDao;
import org.flexpay.orgs.dao.ServiceProviderDaoExt;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

@Transactional (readOnly = true)
public class ServiceProviderServiceImpl implements ServiceProviderService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceProviderDao serviceProviderDao;
	private ServiceProviderDaoExt serviceProviderDaoExt;

	private SessionUtils sessionUtils;
	private ModificationListener<ServiceProvider> modificationListener;

	/**
	 * Read full service provider info
	 *
	 * @param stub provider stub
	 * @return ServiceProvider
	 */
	@SuppressWarnings ({"unchecked"})
    @Override
	public <T extends ServiceProvider> T read(@NotNull Stub<T> stub) {
		log.debug("Service provider read {}", stub);
		return (T) serviceProviderDao.readFull(stub.getId());
	}

	/**
	 * Find service provider by its organization stub
	 *
	 * @param orgStub Service provider organization stub
	 * @return ServiceProvider instance
	 * @throws IllegalArgumentException if provider cannot be found
	 */
    @Override
	public ServiceProvider getProvider(@NotNull Stub<Organization> orgStub) throws IllegalArgumentException {
		ServiceProvider serviceProvider = serviceProviderDaoExt.findByNumber(orgStub.getId());
		if (serviceProvider == null) {
			throw new IllegalArgumentException("Cannot find service provider with number #" + orgStub);
		}

		return serviceProvider;
	}

	/**
	 * List service providers
	 *
	 * @param pager Page
	 * @return List of service providers
	 */
	@NotNull
    @Override
	public List<ServiceProvider> listInstances(@NotNull Page<ServiceProvider> pager) {
		return serviceProviderDao.findProviders(pager);
	}

    /**
	 * List service providers
	 *
	 * @param range    Fetch range
        * @return List of service providers
	 */
	@NotNull
    @Override
	public List<ServiceProvider> listInstances(@NotNull FetchRange range) {
		return serviceProviderDao.listInstancesWithIdentities(range);
	}

	/**
	 * Disable service providers
	 *
	 * @param objectIds Set of service provider identifiers
	 */
	@Transactional (readOnly = false)
    @Override
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			ServiceProvider provider = serviceProviderDao.read(id);
			if (provider != null) {
				provider.disable();
				serviceProviderDao.update(provider);

				modificationListener.onDelete(provider);
			}
		}
	}

	/**
	 * Save service provider
	 *
	 * @param serviceProvider New or persistent object to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if provider validation fails
	 */
	@NotNull
	@Transactional (readOnly = false)
    @Override
	public ServiceProvider create(@NotNull ServiceProvider serviceProvider) throws FlexPayExceptionContainer {
		validate(serviceProvider);
		serviceProvider.setId(null);

		serviceProviderDao.create(serviceProvider);
		modificationListener.onCreate(serviceProvider);

		return serviceProvider;
	}

	/**
	 * Update service provider
	 *
	 * @param serviceProvider object to update
	 * @return updated object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if provider validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Transactional (readOnly = false)
    @Override
	public ServiceProvider update(@NotNull ServiceProvider serviceProvider) throws FlexPayExceptionContainer {
		validate(serviceProvider);

		ServiceProvider old = read(stub(serviceProvider));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + serviceProvider));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, serviceProvider);

		serviceProviderDao.update(serviceProvider);
		return serviceProvider;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(ServiceProvider sp) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		log.info("Provider organization: {}", sp.getOrganization());

		if (sp.getOrganization() == null || sp.getOrganization().isNew()) {
			container.addException(new FlexPayException(
					"No organization selected", "orgs.error.service_provider.no_organization_specified"));
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
					"No default lang desc", "orgs.error.service_provider.no_default_lang_description"));
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
	@NotNull
    @Override
	public OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull ServiceProvider sp) {
		List<Organization> organizations = serviceProviderDao.findProviderlessOrgs();
		List<Organization> providerlessOrgs = list();
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
    @Override
	public ServiceProviderFilter initServiceProvidersFilter(ServiceProviderFilter filter) {

		if (filter == null) {
			filter = new ServiceProviderFilter();
		}

		List<ServiceProvider> providers = serviceProviderDao.findProviders(new Page<ServiceProvider>(10000, 1));
		filter.setInstances(providers);

		return filter;
	}

	/**
	 * Test method that deletes created instance
	 *
	 * @param instance Organization instance to delete
	 */
	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull ServiceProvider instance) {
		serviceProviderDao.delete(instance);
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
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<ServiceProvider> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
