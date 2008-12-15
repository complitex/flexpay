package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.eirc.dao.ServiceDao;
import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.dao.ServiceProviderDao;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.ParentServiceFilterMarker;
import org.flexpay.eirc.persistence.filters.ServiceFilter;
import org.flexpay.eirc.persistence.filters.ServiceProviderFilter;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class SPServiceImpl implements SPService {

	private Logger log = Logger.getLogger(getClass());

	private ServiceProviderDao serviceProviderDao;
	private ServiceDaoExt serviceDaoExt;
	private ServiceDao serviceDao;

	private SessionUtils sessionUtils;
	private DataSourceDescriptionDao dataSourceDescriptionDao;

	private MeasureUnitService measureUnitService;

	/**
	 * Find service provider by its number
	 *
	 * @param providerNumber Service provider unique number
	 * @return ServiceProvider instance
	 * @throws IllegalArgumentException if provider cannot be found
	 */
	public ServiceProvider getProvider(Long providerNumber) throws IllegalArgumentException {
		ServiceProvider serviceProvider = serviceDaoExt.findByNumber(providerNumber);
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
	 * Read full service provider info
	 *
	 * @param provider Service Provider stub
	 * @return ServiceProvider
	 */
	public ServiceProvider read(ServiceProvider provider) {
		if (provider.isNotNew()) {
			return serviceProviderDao.readFull(provider.getId());
		}

		return new ServiceProvider(0L);
	}

	/**
	 * Save service provider
	 *
	 * @param serviceProvider New or persitent object to save
	 * @throws FlexPayExceptionContainer if provider validation fails
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

		if (log.isInfoEnabled()) {
			log.info("Provider organization: " + sp.getOrganization());
		}

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
		Long orgId = sp.getOrganization() != null ? sp.getOrganization().getId() : null;
		OUTER:
		for (Organization org : organizations) {
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
		filter.setProviders(providers);

		return filter;
	}

	/**
	 * List active services using filters and pager
	 *
	 * @param filters Set of filters to apply
	 * @param pager   Page
	 * @return List of services
	 */
	public List<Service> listServices(List<ObjectFilter> filters, Page<Service> pager) {
		return serviceDaoExt.findServices(filters, pager);
	}

	/**
	 * Create or update service
	 *
	 * @param service Service to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(Service service) throws FlexPayExceptionContainer {
		validate(service);
		if (service.isNew()) {
			service.setId(null);
			serviceDao.create(service);
		} else {
			serviceDao.update(service);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(Service service) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (service.getServiceProvider().isNew()) {
			container.addException(new FlexPayException("Connot set empty service provider",
					"eirc.error.service.need_setup_service_provider"));
		}

		if (service.getServiceType().isNew()) {
			container.addException(new FlexPayException("Connot set empty service type",
					"eirc.error.service.need_setup_service_type"));
		}

		if (service.getParentService() != null && service.getParentService().isNew()) {
			container.addException(new FlexPayException("Connot set new parent service",
					"eirc.error.service.invalid_parent_service"));
		}

		if (!service.getChildServices().isEmpty() && service.isSubService()) {
			container.addException(new FlexPayException("Connot set subservices of subservice",
					"eirc.error.service.subservice_cannot_have_subservices"));
		}

		if (service.isSubService() && service.getServiceProvider().isNotNew() && service.getParentService().isNotNew()) {
			Service parentStub = service.getParentService();
			Service parent = serviceDao.read(parentStub.getId());
			Long parentProviderId = parent.getServiceProvider().getId();
			if (!parentProviderId.equals(service.getServiceProvider().getId())) {
				container.addException(new FlexPayException("Subservice wrong provider",
						"eirc.error.service.invalid_parent_service_provider"));
			}
		}

		boolean defaultDescFound = false;
		for (ServiceDescription description : service.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.service.no_default_lang_description"));
		}

		if (StringUtils.isNotBlank(service.getExternalCode())) {
			List<Service> services = serviceDao.findServicesByProviderCode(
					service.getServiceProvider().getId(), service.getExternalCode());
			boolean hasDuplicateCode = services.size() == 1 && !services.get(0).getId().equals(service.getId());
			hasDuplicateCode = hasDuplicateCode || services.size() >= 2;
			if (hasDuplicateCode) {
				container.addException(new FlexPayException(
						"Duplicate code", "eirc.error.service.duplicate_code"));
			}
			sessionUtils.evict(services);
		}

		List<Service> sameTypeSrvcs = serviceDaoExt.findIntersectingServices(
				service.getServiceProvider().getId(), service.getServiceType().getId(),
				service.getBeginDate(), service.getEndDate());
		boolean intersects = sameTypeSrvcs.size() == 1 && !sameTypeSrvcs.get(0).getId().equals(service.getId());
		intersects = intersects || sameTypeSrvcs.size() >= 2;
		if (intersects) {
			container.addException(new FlexPayException(
					"Duplicate service type", "eirc.error.service.duplicate_service_type"));
		}
		sessionUtils.evict(sameTypeSrvcs);

		if (!container.isEmpty()) {
			throw container;
		}
	}

	/**
	 * Read full service information
	 *
	 * @param stub Service stub
	 * @return Service description
	 */
	@Nullable
	public Service read(@NotNull Stub<Service> stub) {
		Service service = serviceDao.readFull(stub.getId());
		if (service != null && service.getMeasureUnit() != null) {
			Stub<MeasureUnit> unitStub = Stub.stub(service.getMeasureUnit());
			service.setMeasureUnit(measureUnitService.read(unitStub));
		}

		return service;
	}

	/**
	 * Initalize service filter with a list of parent services
	 *
	 * @param filter Filter to initialize
	 * @return Filter back
	 */
	public ServiceFilter initParentServicesFilter(ServiceFilter filter) {
		List<ObjectFilter> filters = new ArrayList<ObjectFilter>();
		filters.add(new ParentServiceFilterMarker());
		List<Service> services = serviceDaoExt.findServices(filters, new Page<Service>(10000, 1));
		filter.setServices(services);

		return filter;
	}

	/**
	 * Setter for property 'serviceTypeDaoExt'.
	 *
	 * @param serviceDaoExt Value to set for property 'serviceTypeDaoExt'.
	 */
	public void setServiceDaoExt(ServiceDaoExt serviceDaoExt) {
		this.serviceDaoExt = serviceDaoExt;
	}

	public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
		this.serviceProviderDao = serviceProviderDao;
	}

	public void setDataSourceDescriptionDao(DataSourceDescriptionDao dataSourceDescriptionDao) {
		this.dataSourceDescriptionDao = dataSourceDescriptionDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}
}
