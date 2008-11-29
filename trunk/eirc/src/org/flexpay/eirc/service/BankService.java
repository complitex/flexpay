package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface BankService {

	/**
	 * List registered banks
	 *
	 * @param pager Page
	 * @return List of registered banks
	 */
	@NotNull
	List<Bank> listBanks(@NotNull Page<Bank> pager);

	/**
	 * Disable banks
	 *
	 * @param objectIds Banks identifiers to disable
	 */
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full bank info
	 *
	 * @param stub Bank stub
	 * @return Bank
	 */
	@Nullable
	Bank read(@NotNull Bank stub);

	/**
	 * Save or update bank
	 *
	 * @param bank Bank to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(@NotNull Bank bank) throws FlexPayExceptionContainer;

	/**
	 * Initialize organizations filter, includes only organizations that are not banks or this particular <code>bank</code>
	 * organization
	 *
	 * @param organizationFilter Filter to initialize
	 * @param bank Bank
	 */
	void initBanklessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull Bank bank);

}
