package org.flexpay.eirc.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.commons.collections.ArrayStack;

import java.util.List;

public interface EircAccountService {

	/**
	 * Find EircAccount by person and apartment references
	 *
	 * @param personStub	Person reference
	 * @param apartmentStub Apartment reference
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	EircAccount findAccount(@NotNull Stub<Person> personStub, @NotNull Stub<Apartment> apartmentStub);

	/**
	 * Create or update account
	 *
	 * @param account EIRC account to save
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	void save(@NotNull EircAccount account) throws FlexPayExceptionContainer;

	/**
	 * Get next personal account number
	 *
	 * @return Account number
	 */
	String nextPersonalAccount();

	/**
	 * Find EircAccounts
	 *
	 * @param filters Filters stack
	 * @param pager Accounts pager
	 * @return List of EircAccount
	 */
	List<EircAccount> findAccounts(ArrayStack filters, Page<EircAccount> pager);

	/**
	 * Read full account info, includes person and service
	 *
	 * @param stub Account stub
	 * @return EircAccount if found, or <code>null</code> if stub references no object
	 */
	@Nullable
	EircAccount readFull(@NotNull Stub<EircAccount> stub);
}
