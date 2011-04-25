package org.flexpay.orgs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.dao.ServiceOrganizationDao;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceOrganizationFilter;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class ServiceOrganizationServiceImpl implements ServiceOrganizationService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private SessionUtils sessionUtils;
	private ServiceOrganizationDao serviceOrganizationDao;
	private ModificationListener<ServiceOrganization> modificationListener;

	/**
	 * Read ServiceOrganization object by its unique id
	 *
	 * @param stub ServiceOrganization key
	 * @return ServiceOrganization object, or <code>null</code> if object not found
	 */
	@SuppressWarnings ({"unchecked"})
	public <T extends ServiceOrganization> T read(@NotNull Stub<T> stub) {
		log.debug("ReadFull called");
		return (T) serviceOrganizationDao.readFull(stub.getId());
	}

	/**
	 * Disable service organizations
	 *
	 * @param objectIds Service organization identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			ServiceOrganization sOrganization = serviceOrganizationDao.read(id);
			if (sOrganization != null) {
				sOrganization.disable();
				serviceOrganizationDao.update(sOrganization);

				modificationListener.onDelete(sOrganization);
				log.debug("Disabled instance: {}", sOrganization);
			}
		}
	}

	/**
	 * Create or update service organization
	 *
	 * @param serviceOrganization Service organization to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	@Transactional (readOnly = false)
	public ServiceOrganization create(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer {
		validate(serviceOrganization);
		serviceOrganization.setId(null);
		serviceOrganizationDao.create(serviceOrganization);

		modificationListener.onCreate(serviceOrganization);

		return serviceOrganization;
	}

	/**
	 * Update service organization
	 *
	 * @param serviceOrganization Service organization to save
	 * @return updated instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Transactional (readOnly = false)
	public ServiceOrganization update(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer {

		validate(serviceOrganization);

		ServiceOrganization old = read(stub(serviceOrganization));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + serviceOrganization));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, serviceOrganization);

		serviceOrganizationDao.update(serviceOrganization);

		return serviceOrganization;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull ServiceOrganization organization) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultDescFound = false;
		for (ServiceOrganizationDescription description : organization.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "orgs.error.service_organization.no_default_lang_description"));
		}

		List<ServiceOrganization> organizationServiceOrganizations = serviceOrganizationDao
				.findOrganizationServiceOrganizations(organization.getOrganizationStub().getId());
		if (organizationServiceOrganizations.size() > 1) {
			container.addException(new FlexPayException("Several service organizations found",
					"orgs.error.service_organization.several_service_organizations_in_organization"));
		}
		if (!organizationServiceOrganizations.isEmpty()) {
			ServiceOrganization existingServiceOrganization = organizationServiceOrganizations.get(0);
			if (!existingServiceOrganization.equals(organization)) {
				container.addException(new FlexPayException("Service organization already exists",
						"orgs.error.service_organization.organization_already_has_service_organization"));
			}
		}
		sessionUtils.evict(organizationServiceOrganizations);

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceOrganizationFilter to initialize
	 * @return ServiceOrganizationFilter back
	 */
	public ServiceOrganizationFilter initServiceOrganizationsFilter(ServiceOrganizationFilter filter) {

		if (filter == null) {
			filter = new ServiceOrganizationFilter();
		}

		List<ServiceOrganization> organizations = serviceOrganizationDao.listServiceOrganizations();
		filter.setInstances(organizations);

		return filter;
	}

	/**
	 * Initialize organizations filter, includes only organizations that are not banks or this particular <code>service
	 * organization</code> organization
	 *
	 * @param filter Filter to initialize
	 * @param org	service organization
	 */
	@NotNull
	public OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull ServiceOrganization org) {
		@SuppressWarnings ({"UnnecessaryBoxing"})
		Long includedServiceOrganizationId = org.isNotNew() ? org.getId() : Long.valueOf(-1L);
		List<Organization> organizations = serviceOrganizationDao
				.findServiceOrganizationlessOrganizations(includedServiceOrganizationId);
		filter.setOrganizations(organizations);

		return filter;
	}

	/**
	 * List registered service organizations
	 *
	 * @param pager Page
	 * @return List of registered service organizations
	 */
	@NotNull
	public List<ServiceOrganization> listInstances(@NotNull Page<ServiceOrganization> pager) {
		return serviceOrganizationDao.findServiceOrganizations(pager);
	}

	/**
	 * Get a list of available ServiceOrganization
	 *
	 * @return List of ServiceOrganization
	 */
	@NotNull
	public List<ServiceOrganization> listServiceOrganizations() {
		return serviceOrganizationDao.listServiceOrganizations();
	}

	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull ServiceOrganization org) {
		serviceOrganizationDao.delete(org);
	}

	@Required
	public void setServiceOrganizationDao(ServiceOrganizationDao serviceOrganizationDao) {
		this.serviceOrganizationDao = serviceOrganizationDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<ServiceOrganization> modificationListener) {
		this.modificationListener = modificationListener;
	}
}
