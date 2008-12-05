package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.eirc.dao.ServedBuildingDao;
import org.flexpay.eirc.dao.ServiceOrganizationDao;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.ServiceOrganizationDescription;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.persistence.filters.ServiceOrganizationFilter;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceOrganizationServiceImpl implements ServiceOrganizationService {

    @NonNls
    private Logger log = Logger.getLogger(getClass());

    private SessionUtils sessionUtils;
	private ServiceOrganizationDao serviceOrganizationDao;
    private ServedBuildingDao servedBuildingDao;

	/**
	 * Read ServiceOrganization object by its unique id
	 *
	 * @param stub ServiceOrganization key
	 * @return ServiceOrganization object, or <code>null</code> if object not found
	 */
	public ServiceOrganization read(@NotNull Stub<ServiceOrganization> stub) {
		return serviceOrganizationDao.readFull(stub.getId());
	}

    public List<ServedBuilding> findServedBuildings(@NotNull Collection<Long> ids) {
        return servedBuildingDao.findServedBuildings(ids);
    }

    public List<ServedBuilding> findServedBuildings(@NotNull Stub<ServiceOrganization> stub, Page<ServedBuilding> pager) {
        return servedBuildingDao.findServedBuildingsByServiceOrganization(stub.getId(), pager);
    }

    @Transactional (readOnly = false)
    public void removeServedBuildings(@NotNull Set<Long> objectIds) {
        for (Long id : objectIds) {
            ServedBuilding building = servedBuildingDao.read(id);
            if (building != null) {
                building.setServiceOrganization(null);
                servedBuildingDao.update(building);
            }
        }
    }

    /**
     * Create or update served building
     *
     * @param servedBuilding Served building to save
     * @throws org.flexpay.common.exception.FlexPayExceptionContainer
     *          if validation fails
     */
    @Transactional (readOnly = false)
    public void saveServedBuilding(@NotNull ServedBuilding servedBuilding) {
        if (servedBuilding.isNew()) {
            servedBuilding.setId(null);
            servedBuildingDao.create(servedBuilding);
        } else {
            servedBuildingDao.update(servedBuilding);
        }
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
    @Transactional (readOnly = false)
    public void save(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer {
        validate(serviceOrganization);
        if (serviceOrganization.isNew()) {
            serviceOrganization.setId(null);
            serviceOrganizationDao.create(serviceOrganization);
        } else {
            serviceOrganizationDao.update(serviceOrganization);
        }
    }

    @SuppressWarnings ({"ThrowableInstanceNeverThrown"})
    private void validate(@NotNull ServiceOrganization serviceOrganization) throws FlexPayExceptionContainer {
        FlexPayExceptionContainer container = new FlexPayExceptionContainer();

        boolean defaultDescFound = false;
        for (ServiceOrganizationDescription description : serviceOrganization.getDescriptions()) {
            if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
                defaultDescFound = true;
            }
        }
        if (!defaultDescFound) {
            container.addException(new FlexPayException(
                    "No default lang desc", "eirc.error.service_organization.no_default_lang_description"));
        }

        List<ServiceOrganization> organizationServiceOrganizations = serviceOrganizationDao.findOrganizationServiceOrganizations(serviceOrganization.getOrganization().getId());
        if (organizationServiceOrganizations.size() > 1) {
            container.addException(new FlexPayException(
                    "Several service organizations found", "eirc.error.service_organization.several_service_organizations_in_organization"));
        }
        if (!organizationServiceOrganizations.isEmpty()) {
            ServiceOrganization existingServiceOrganization = organizationServiceOrganizations.get(0);
            if (!existingServiceOrganization.equals(serviceOrganization)) {
                container.addException(new FlexPayException(
                        "Service organization already exists", "eirc.error.service_organization.organasation_already_has_service_organization"));
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
		filter.setOrganizations(organizations);

		return filter;
	}

    /**
     * Initialize organizations filter, includes only organizations that are not banks or this particular <code>service organization</code>
     * organization
     *
     * @param organizationFilter Filter to initialize
     * @param serviceOrganization service organization
     */
    public void initServiceOrganizationlessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull ServiceOrganization serviceOrganization) {
        Long includedServiceOrganizationId = serviceOrganization.isNotNew() ? serviceOrganization.getId() : Long.valueOf(-1L);
        List<Organization> organizations = serviceOrganizationDao.findServiceOrganizationlessOrganizations(includedServiceOrganizationId);
        organizationFilter.setOrganizations(organizations);
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
    public List<ServiceOrganization> listServiceOrganizations(Page<ServiceOrganization> pager) {
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

	/**
	 * @param serviceOrganizationDao the serviceOrganizationDao to set
	 */
	public void setServiceOrganizationDao(ServiceOrganizationDao serviceOrganizationDao) {
		this.serviceOrganizationDao = serviceOrganizationDao;
	}

    public void setServedBuildingDao(ServedBuildingDao servedBuildingDao) {
        this.servedBuildingDao = servedBuildingDao;
    }

    public void setSessionUtils(SessionUtils sessionUtils) {
        this.sessionUtils = sessionUtils;
    }

}
