package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.Bank;

public interface BankDao extends GenericDao<Bank, Long> {


	/**
	 * Find banks
	 *
	 * @param pager Page
	 * @return list banks
	 */
	List<Bank> findBanks(Page<Bank> pager);

	/**
	 * Find banks for organisation
	 *
	 * @param organisationId Organisation key
	 * @return List of banks for organisation 
	 */
	List<Bank> findOrganisationBanks(Long organisationId);

	/**
	 * Find organisations that are not banks except of that has a bank with specified <code>includedBankId</code
	 * >
	 * @param includedBankId Allowed bank key, that organisation will also be in a resulting list
	 * @return List of organisations that are not banks
	 */
	List<Organisation> findBanklessOrganisations(Long includedBankId);
}