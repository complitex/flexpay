package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.DataSourceDescriptionDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.dao.ServiceProviderDao;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.service.SPService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class SPServiceImpl implements SPService {

	private Logger log = Logger.getLogger(getClass());

	private ServiceProviderDao serviceProviderDao;
	private ServiceDaoExt serviceDaoExt;

	private DataSourceDescriptionDao dataSourceDescriptionDao;

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
	 * Find service of specified <code>type</code> for provider
	 *
	 * @param provider ServiceProvider
	 * @param type	 ServiceType to find
	 * @return Service if found, or <code>null</code> if the requested service is not
	 *         available from <code>provider</code>
	 */
	public Service getService(ServiceProvider provider, ServiceType type) {
		Service service = serviceDaoExt.findService(provider.getId(), type.getId());
		if (service == null) {
			throw new IllegalArgumentException(
					"Cannot find service (code " + type.getCode() + ") for provider" + provider.getId());
		}

		return service;
	}

	/**
	 * Get record type by type id
	 *
	 * @param typeId Record type enum id
	 * @return record type
	 */
	public AccountRecordType getRecordType(int typeId) {
		AccountRecordType type = serviceDaoExt.findRecordType(typeId);
		if (type == null) {
			throw new IllegalArgumentException("Cannot find record type #" + typeId);
		}

		return type;
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
	@Transactional(readOnly = false)
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
	@Transactional(readOnly = false)
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

	private void validate(ServiceProvider sp) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (log.isInfoEnabled()) {
			log.info("Provider organisation: " + sp.getOrganisation());
		}

		if (sp.getOrganisation() == null || sp.getOrganisation().isNew()) {
			container.addException(new FlexPayException(
					"No organisation selected", "eirc.error.service_provider.no_organisation_specified"));
		}
		// todo validate organisation id was not changed for existing provider

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
	 * Initialize filter with organisations that do not have active service providers
	 * <p/>
	 * todo: implement in a more efficient way
	 *
	 * @param organisationFilter filter to init
	 * @param sp				 Service Provider
	 * @return filter
	 */
	public OrganisationFilter initOrganisationFilter(OrganisationFilter organisationFilter, ServiceProvider sp) {
		List<Organisation> organisations = serviceProviderDao.findProviderlessOrgs();
		List<Organisation> providerlessOrgs = new ArrayList<Organisation>();
		Long orgId = sp.getOrganisation() != null ? sp.getOrganisation().getId() : null;
		OUTER:
		for (Organisation org : organisations) {
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

		organisationFilter.setOrganisations(providerlessOrgs);
		if (orgId != null && !orgId.equals(0L)) {
			organisationFilter.setReadOnly(true);
			// todo ensure organisation really exists
			organisationFilter.setSelectedId(orgId);
		}

		return organisationFilter;
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
}
