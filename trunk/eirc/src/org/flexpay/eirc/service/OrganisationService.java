package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface OrganisationService {

	/**
	 * Find organisation by its id
	 *
	 * @param stub Organisation stub
	 * @return Organisation if found, or <code>null</code> otherwise
	 */
	@Nullable
	Organisation getOrganisation(Stub<Organisation> stub);

	/**
	 * Initialize organisations filter
	 *
	 * @param organisationFilter Filter to initialize
	 */
	void initFilter(OrganisationFilter organisationFilter);

	/**
	 * List registered organisations
	 *
	 * @param pager Page
	 * @return List of registered organisations
	 */
	List<Organisation> listOrganisations(Page<Organisation> pager);

	/**
	 * Disable organisations
	 *
	 * @param objectIds Organisations identifiers to disable
	 */
	void disable(Set<Long> objectIds);

	/**
	 * Read full organisation info
	 *
	 * @param stub Organisation stub
	 * @return Organisation
	 */
	Organisation read(Organisation stub);

	/**
	 * Save or update organisation
	 *
	 * @param organisation Organisation to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(Organisation organisation) throws FlexPayExceptionContainer;
}
