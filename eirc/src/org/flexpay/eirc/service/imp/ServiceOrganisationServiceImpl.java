package org.flexpay.eirc.service.imp;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.ServiceOrganisationDao;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.filters.ServiceOrganisationFilter;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class ServiceOrganisationServiceImpl implements ServiceOrganisationService {

	private ServiceOrganisationDao serviceOrganisationDao;

	/**
	 * Read ServiceOrganisation object by its unique id
	 *
	 * @param stub ServiceOrganisation key
	 * @return ServiceOrganisation object, or <code>null</code> if object not found
	 */
	@Transactional (readOnly = true)
	public ServiceOrganisation read(@NotNull Stub<ServiceOrganisation> stub) {
		return serviceOrganisationDao.readFull(stub.getId());
	}

	@Transactional (readOnly = true)
	public Set<ServedBuilding> findServedBuildings(@NotNull Stub<ServiceOrganisation> stub) {
		return serviceOrganisationDao.findServedBuildings(stub.getId());
	}

	/**
	 * Initialize filter
	 *
	 * @param filter ServiceOrganisationFilter to initialize
	 * @return ServiceOrganisationFilter back
	 */
	@Transactional (readOnly = true)
	public ServiceOrganisationFilter initServiceOrganisationsFilter(ServiceOrganisationFilter filter) {

		if (filter == null) {
			filter = new ServiceOrganisationFilter();
		}

		List<ServiceOrganisation> organisations = serviceOrganisationDao.listServiceOrganisation();
		filter.setOrganisations(organisations);

		return filter;
	}

	/**
	 * Get a list of available ServiceOrganisation
	 *
	 * @return List of ServiceOrganisation
	 */
	@Transactional (readOnly = true)
	@NotNull
	public List<ServiceOrganisation> listServiceOrganisations() {
		return serviceOrganisationDao.listServiceOrganisation();
	}

	/**
	 * @param serviceOrganisationDao the serviceOrganisationDao to set
	 */
	public void setServiceOrganisationDao(ServiceOrganisationDao serviceOrganisationDao) {
		this.serviceOrganisationDao = serviceOrganisationDao;
	}
}
