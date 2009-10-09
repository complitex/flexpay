package org.flexpay.eirc.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;

public interface EircAccountService {

	/**
	 * Find EircAccount by person and apartment references
	 *
	 * @param personStub	Person reference
	 * @param apartmentStub Apartment reference
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.ACCOUNT_READ)
	EircAccount findAccount(@NotNull Stub<Person> personStub, @NotNull Stub<Apartment> apartmentStub);

	/**
	 * Create or update account
	 *
	 * @param account EIRC account to save
	 * @return persisted account object back
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	@Secured (Roles.ACCOUNT_ADD)
	@NotNull
	EircAccount create(@NotNull EircAccount account) throws FlexPayExceptionContainer;

	/**
	 * Update account
	 *
	 * @param account EIRC account to save
	 * @return updated account object back
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	@Secured (Roles.ACCOUNT_CHANGE)
	@NotNull
	EircAccount update(@NotNull EircAccount account) throws FlexPayExceptionContainer;

	/**
	 * Find EircAccounts
	 *
	 * @param filters Filters stack
	 * @param pager   Accounts pager
	 * @return List of EircAccount
	 */
	@Secured (Roles.ACCOUNT_READ)
	List<EircAccount> findAccounts(ArrayStack filters, Page<EircAccount> pager);

	/**
	 * Find EircAccounts
	 *
	 * @param stub Stub of apartment
	 * @param personFio query for person FIO search
	 * @param pager   Accounts pager
	 *
	 * @return List of EircAccount
	 */
	@Secured (Roles.ACCOUNT_READ)
	List<EircAccount> getAccountsInApartment(@NotNull Stub<Apartment> stub, @NotNull String personFio, Page<EircAccount> pager);

	/**
	 * Find EircAccounts
	 *
	 * @param stub Stub of building
	 * @param personFio query for person FIO search
	 * @param pager   Accounts pager
	 *
	 * @return List of EircAccount
	 */
	@Secured (Roles.ACCOUNT_READ)
	List<EircAccount> getAccountsInBuilding(@NotNull Stub<BuildingAddress> stub, @NotNull String personFio, Page<EircAccount> pager);

	/**
	 * Read full account info, includes person and service
	 *
	 * @param stub Account stub
	 * @return EircAccount if found, or <code>null</code> if stub references no object
	 */
	@Secured (Roles.ACCOUNT_READ)
	@Nullable
	EircAccount readFull(@NotNull Stub<EircAccount> stub);

	/**
	 * Get person FIO that account was created for
	 *
	 * @param account EircAccount to get person last-first-middle names for
	 * @return person last-first-middle names if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.ACCOUNT_READ)
	@Nullable
	String getPersonFIO(@NotNull EircAccount account);

	/**
	 * Find account by its number
	 *
	 * @param accountNumber EircAccount number to lookup
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.ACCOUNT_READ)
	@Nullable
	EircAccount findAccount(String accountNumber);
}
