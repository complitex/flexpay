package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface ServiceOrganisationService {

	/**
	 * Get a list of available ServiceOrganisations
	 *
	 * @return List of ServiceOrganisations
	 */
	@NotNull
	List<ServiceOrganisation> listServiceOrganisations();

	/**
	 * Read ServiceOrganisation object by its unique id
	 *
	 * @param stub ServiceOrganisation stub
	 * @return ServiceOrganisation object, or <code>null</code> if object not found
	 */
	ServiceOrganisation read(@NotNull Stub<ServiceOrganisation> stub);


	/**
	 * Get a ServiceOrganisation with organisation, buildings, apartaments, persons
	 *
	 * @param id ServiceOrganisation key
	 * @return ServiceOrganisation
	 */
	ServiceOrganisation readForTicketGeneration(Long id);

	/**
	 * 
	 * @param stub ServiceOrganisation stub
	 * @return
	 */
	Set<ServedBuilding> findServedBuildings(@NotNull Stub<ServiceOrganisation> stub);
}
