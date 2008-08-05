package org.flexpay.eirc.service.imp;

import java.util.List;
import java.util.Set;

import org.flexpay.eirc.dao.ServiceOrganisationDao;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.common.persistence.Stub;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.NotNull;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ServiceOrganisationServiceImpl implements
		ServiceOrganisationService {

	private ServiceOrganisationDao serviceOrganisationDao;
	
	/**
	 * Read ServiceOrganisation object by its unique id
	 *
	 * @param stub ServiceOrganisation key
	 * @return ServiceOrganisation object, or <code>null</code> if object not found
	 */
	public ServiceOrganisation read(@NotNull Stub<ServiceOrganisation> stub) {
		return serviceOrganisationDao.read(stub.getId());
	}
	
	public Set<ServedBuilding> findServedBuildings(@NotNull Stub<ServiceOrganisation> stub) {
		return serviceOrganisationDao.findServedBuildings(stub.getId());
	}

	/**
	 * Get a list of available ServiceOrganisation
	 * 
	 * @return List of ServiceOrganisation
	 */
	@NotNull
	public List<ServiceOrganisation> listServiceOrganisations() {
		return serviceOrganisationDao.listServiceOrganisation();
	}
	
	/**
	 * Get a ServiceOrganisation with organisation, buildings, apartments, persons 
	 *
	 * @return ServiceOrganisation
	 */
	public ServiceOrganisation readForTicketGeneration(Long id) {
		return serviceOrganisationDao.readFull(id);
	}

	/**
	 * @param serviceOrganisationDao
	 *            the serviceOrganisationDao to set
	 */
	public void setServiceOrganisationDao(ServiceOrganisationDao serviceOrganisationDao) {
		this.serviceOrganisationDao = serviceOrganisationDao;
	}
}
