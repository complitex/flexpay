package org.flexpay.orgs.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.orgs.dao.ServiceOrganizationDao;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceOrganizationFilter;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceOrganizationServiceImpl implements ServiceOrganizationService {

	private SessionUtils sessionUtils;
	private ServiceOrganizationDao serviceOrganizationDao;

	/**
	 * Read ServiceOrganization object by its unique id
	 *
	 * @param stub ServiceOrganization key
	 * @return ServiceOrganization object, or <code>null</code> if object not found
	 */
	public ServiceOrganization read(@NotNull Stub<ServiceOrganization> stub) {
		return serviceOrganizationDao.readFull(stub.getId());
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
		if (serviceOrganization.isNew()) {
			serviceOrganization.setId(null);
			serviceOrganizationDao.create(serviceOrganization);
		} else {
			serviceOrganizationDao.update(serviceOrganization);
		}

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
	@NotNull
	public ServiceOrganization update(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer {

		validate(serviceOrganization);
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
					"No default lang desc", "eirc.error.service_organization.no_default_lang_description"));
		}

		List<ServiceOrganization> organizationServiceOrganizations = serviceOrganizationDao
				.findOrganizationServiceOrganizations(organization.getOrganizationStub().getId());
		if (organizationServiceOrganizations.size() > 1) {
			container.addException(new FlexPayException("Several service organizations found",
					"eirc.error.service_organization.several_service_organizations_in_organization"));
		}
		if (!organizationServiceOrganizations.isEmpty()) {
			ServiceOrganization existingServiceOrganization = organizationServiceOrganizations.get(0);
			if (!existingServiceOrganization.equals(organization)) {
				container.addException(new FlexPayException("Service organization already exists",
						"eirc.error.service_organization.organasation_already_has_service_organization"));
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
	 * @param filter  Filter to initialize
	 * @param org service organization
	 */
	public OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter filter, @NotNull ServiceOrganization org) {
		@SuppressWarnings ({"UnnecessaryBoxing"})
		Long includedServiceOrganizationId = org.isNotNew() ? org.getId() : Long.valueOf(-1L);
		List<Organization> organizations = serviceOrganizationDao
				.findServiceOrganizationlessOrganizations(includedServiceOrganizationId);
		filter.setOrganizations(organizations);

		return filter;
	}

	/**
	 * Read full service organization info
	 *
	 * @param stub Service organization stub
	 * @return ServiceOrganization
	 */
	@Nullable
	public ServiceOrganization read(@NotNull ServiceOrganization stub) {
		if (stub.isNotNew()) {
			return serviceOrganizationDao.readFull(new Stub<ServiceOrganization>(stub).getId());
		}

		return new ServiceOrganization(0L);
	}

	/**
	 * List registered service organizations
	 *
	 * @param pager Page
	 * @return List of registered service organizations
	 */
	@NotNull
	public List<ServiceOrganization> listInstances(Page<ServiceOrganization> pager) {
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

	@Required
	public void setServiceOrganizationDao(ServiceOrganizationDao serviceOrganizationDao) {
		this.serviceOrganizationDao = serviceOrganizationDao;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}
}
