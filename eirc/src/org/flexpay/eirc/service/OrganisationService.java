package org.flexpay.eirc.service;

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
