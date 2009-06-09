package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface OrganizationService {

	/**
	 * Find organization by its id
	 *
	 * @param stub Organization stub
	 * @return Organization if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.ORGANIZATION_READ)
	@Nullable
	Organization readFull(Stub<Organization> stub);

	/**
	 * Initialize organizations filter
	 *
	 * @param organizationFilter Filter to initialize
	 */
	@Secured (Roles.ORGANIZATION_READ)
	void initFilter(OrganizationFilter organizationFilter);

	/**
	 * List registered organizations
	 *
	 * @param pager Page
	 * @return List of registered organizations
	 */
	@Secured (Roles.ORGANIZATION_READ)
	List<Organization> listOrganizations(Page<Organization> pager);

	/**
	 * List registered organizations which have any {@link org.flexpay.orgs.persistence.PaymentsCollector}s
	 *
	 * @return list of organiztions which has collectors
	 */
	@Secured (Roles.ORGANIZATION_READ)
	List<Organization> listOrganizationsWithCollectors();

	/**
	 * Disable organizations
	 *
	 * @param objectIds Organizations identifiers to disable
	 */
	@Secured (Roles.ORGANIZATION_DELETE)
	void disable(Set<Long> objectIds);

	/**
	 * Create organization
	 *
	 * @param organization Organization to save
	 * @return persisted object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.ORGANIZATION_ADD)
	@NotNull
	Organization create(@NotNull Organization organization) throws FlexPayExceptionContainer;

	/**
	 * Update organization
	 *
	 * @param organization Organization to save
	 * @return Updated object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.ORGANIZATION_CHANGE)
	@NotNull
	Organization update(@NotNull Organization organization) throws FlexPayExceptionContainer;

	/**
	 * Delete Organization object
	 *
	 * @param organizationStub organization stub
	 */
	@Secured (Roles.ORGANIZATION_DELETE)
	void delete(@NotNull Stub<Organization> organizationStub);
}
