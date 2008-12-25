package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.BankDescription;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface BankService extends OrganisationInstanceService<BankDescription, Bank> {

	/**
	 * List registered banks
	 *
	 * @param pager Page
	 * @return List of registered banks
	 */
	@Secured (Roles.BANK_READ)
	@NotNull
	List<Bank> listInstances(@NotNull Page<Bank> pager);

	/**
	 * Disable banks
	 *
	 * @param objectIds Banks identifiers to disable
	 */
	@Secured (Roles.BANK_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full bank info
	 *
	 * @param stub Bank stub
	 * @return Bank
	 */
	@Secured (Roles.BANK_READ)
	@Nullable
	Bank read(@NotNull Stub<Bank> stub);

	/**
	 * Create instance
	 *
	 * @param instance Organisation instance to save
	 * @return saved instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.BANK_ADD)
	@NotNull
	Bank create(@NotNull Bank instance) throws FlexPayExceptionContainer;

	/**
	 * Update instance
	 *
	 * @param instance Organisation instance to save
	 * @return updated instance back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.BANK_CHANGE)
	@NotNull
	Bank update(@NotNull Bank instance) throws FlexPayExceptionContainer;

	/**
	 * Initialize organizations filter, includes only organizations that are not banks or this particular <code>bank</code>
	 * organization
	 *
	 * @param organizationFilter Filter to initialize
	 * @param bank			   Bank
	 * @return filter
	 */
	@Secured (Roles.BANK_READ)
	@NotNull
	OrganizationFilter initInstancelessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull Bank bank);
}
