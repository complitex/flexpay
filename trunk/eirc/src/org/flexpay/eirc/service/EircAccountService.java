package org.flexpay.eirc.service;

import java.util.List;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.EircAccount;

public interface EircAccountService {

	/**
	 * Find EircAccount by person and apartment references
	 *
	 * @param personStub	Person reference
	 * @param apartmentStub Apartment reference
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	EircAccount findAccount(Person personStub, Apartment apartmentStub);

	/**
	 * Create or update account
	 *
	 * @param account EIRC account to save
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	void save(EircAccount account) throws FlexPayExceptionContainer;

	/**
	 * Get next personal account number
	 *
	 * @return Account number
	 */
	String nextPersonalAccount();
	
	/**
	 * Find all EircAccounts
	 *
	 * @return List of EircAccount
	 */
	List<EircAccount> findAll(Page<EircAccount> pager);
}
