package org.flexpay.eirc.service;

import java.util.List;
import java.util.Set;

import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;

public interface ServiceOrganisationService {
	
	/**
	 * Get a list of available ServiceOrganisation
	 *
	 * @return List of ServiceOrganisation
	 */
	List<ServiceOrganisation> listServiceOrganisation();
	
	/**
	 * Read ServiceOrganisation object by its unique id
	 *
	 * @param id ServiceOrganisation key
	 * @return ServiceOrganisation object, or <code>null</code> if object not found
	 */
	ServiceOrganisation read(Long id);

	
	/**
	 * Get a ServiceOrganisation with organisation, buildings, apartaments, persons
	 *  
	 * @param id ServiceOrganisation key
	 * 
	 * @return ServiceOrganisation
	 */
	ServiceOrganisation readForTicketGeneration(Long id);
	
	
	Set<ServedBuilding> findServedBuildings(Long id);
}
