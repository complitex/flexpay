package org.flexpay.eirc.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.eirc.dao.BankDao;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.BankDescription;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.filters.OrganizationFilter;
import org.flexpay.eirc.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class BankServiceImpl implements BankService {

	private SessionUtils sessionUtils;
	private BankDao bankDao;

	/**
	 * List registered banks
	 *
	 * @param pager Page
	 * @return List of registered banks
	 */
	@NotNull
	public List<Bank> listBanks(@NotNull Page<Bank> pager) {
		return bankDao.findBanks(pager);
	}

	/**
	 * Disable banks
	 *
	 * @param objectIds Banks identifiers to disable
	 */
	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			Bank bank = bankDao.read(id);
			if (bank != null) {
				bank.disable();
				bankDao.update(bank);
			}
		}
	}

	/**
	 * Read full bank info
	 *
	 * @param stub Bank stub
	 * @return Bank
	 */
	@Nullable
	public Bank read(@NotNull Bank stub) {
		if (stub.isNotNew()) {
			return bankDao.readFull(new Stub<Bank>(stub).getId());
		}

		return new Bank(0L);

	}

	/**
	 * Save or update bank
	 *
	 * @param bank Bank to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull Bank bank) throws FlexPayExceptionContainer {
		validate(bank);
		if (bank.isNew()) {
			bank.setId(null);
			bankDao.create(bank);
		} else {
			bankDao.update(bank);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Bank bank) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (StringUtils.isBlank(bank.getBankIdentifierCode())) {
			container.addException(new FlexPayException("No BIK", "eirc.error.bank.no_bank_identifier_code"));
		}

		if (StringUtils.isBlank(bank.getCorrespondingAccount())) {
			container.addException(new FlexPayException(
					"No corresponding account", "eirc.error.bank.no_corresponding_account"));
		}

		boolean defaultDescFound = false;
		for (BankDescription description : bank.getDescriptions()) {
			if (description.getLang().isDefault() && StringUtils.isNotBlank(description.getName())) {
				defaultDescFound = true;
			}
		}
		if (!defaultDescFound) {
			container.addException(new FlexPayException(
					"No default lang desc", "eirc.error.bank.no_default_lang_description"));
		}

		List<Bank> organizationBanks = bankDao.findOrganizationBanks(bank.getOrganization().getId());
		if (organizationBanks.size() > 1) {
			container.addException(new FlexPayException(
					"Several banks found", "eirc.error.bank.several_banks_in_organization"));
		}
		if (!organizationBanks.isEmpty()) {
			Bank existingBank = organizationBanks.get(0);
			if (!existingBank.equals(bank)) {
				container.addException(new FlexPayException(
						"Bank already exists", "eirc.error.bank.organasation_already_has_bank"));
			}
		}
		sessionUtils.evict(organizationBanks);

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Initialize organizations filter, includes only organizations that are not banks or this particular <code>bank</code>
	 * organization
	 *
	 * @param organizationFilter Filter to initialize
	 * @param bank Bank
	 */
	@SuppressWarnings ({"UnnecessaryBoxing"})
	public void initBanklessFilter(@NotNull OrganizationFilter organizationFilter, @NotNull Bank bank) {
		Long includedBankId = bank.isNotNew() ? bank.getId() : Long.valueOf(-1L);
		List<Organization> organizations = bankDao.findBanklessOrganizations(includedBankId);
		organizationFilter.setOrganizations(organizations);
	}

	public void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}

	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}
}
