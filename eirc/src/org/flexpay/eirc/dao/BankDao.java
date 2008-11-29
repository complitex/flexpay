package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.persistence.Organization;

import java.util.List;

public interface BankDao extends GenericDao<Bank, Long> {

	/**
	 * Find banks
	 *
	 * @param pager Page
	 * @return list banks
	 */
	List<Bank> findBanks(Page<Bank> pager);

	/**
	 * Find banks for organization
	 *
	 * @param organizationId Organization key
	 * @return List of banks for organization
	 */
	List<Bank> findOrganizationBanks(Long organizationId);

	/**
	 * Find organizations that are not banks except of that has a bank with specified <code>includedBankId</code
	 * >
	 * @param includedBankId Allowed bank key, that organization will also be in a resulting list
	 * @return List of organizations that are not banks
	 */
	List<Organization> findBanklessOrganizations(Long includedBankId);

}
