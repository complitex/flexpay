package org.flexpay.eirc.service;

import java.util.List;

import org.flexpay.eirc.persistence.Organisation;

public interface OrganisationService {

	/**
	 * Find organisation by its id
	 *
	 * @param organisationId Organisation id
	 * @return Organisation if found, or <code>null</code> otherwise
	 */
	Organisation getOrganisation(String organisationId);
	
}
