package org.flexpay.eirc.service;

import java.util.List;

import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;

public interface OrganisationService {

	/**
	 * Find organisation by its id
	 *
	 * @param organisationId Organisation id
	 * @return Organisation if found, or <code>null</code> otherwise
	 */
	Organisation getOrganisation(String organisationId);

	/**
	 * Initialize organisations filter
	 *
	 * @param organisationFilter Filter to initialize
	 */
	void initFilter(OrganisationFilter organisationFilter);
}
