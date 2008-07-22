package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
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
	 * Initialize organisations filter, includes only organisations that are not banks or this particular <code>bank</code>
	 * organisation
	 *
	 * @param organisationFilter Filter to initialize
	 * @param bank Bank
	 */
	void initBanklessFilter(@NotNull OrganisationFilter organisationFilter, @NotNull Bank bank);
}
