package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

	/**
	 * Find organization by its id
	 *
	 * @param stub Organization stub
	 * @return Organization if found, or <code>null</code> otherwise
	 */
	@Nullable
	Organization getOrganization(Stub<Organization> stub);

	/**
	 * Initialize organizations filter
	 *
	 * @param organizationFilter Filter to initialize
	 */
	void initFilter(OrganizationFilter organizationFilter);

	/**
	 * List registered organizations
	 *
	 * @param pager Page
	 * @return List of registered organizations
	 */
	List<Organization> listOrganizations(Page<Organization> pager);

	/**
	 * Disable organizations
	 *
	 * @param objectIds Organizations identifiers to disable
	 */
	void disable(Set<Long> objectIds);

	/**
	 * Read full organization info
	 *
	 * @param stub Organization stub
	 * @return Organization
	 */
	Organization read(Organization stub);

	/**
	 * Save or update organization
	 *
	 * @param organization Organization to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(Organization organization) throws FlexPayExceptionContainer;

}
