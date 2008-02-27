package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.PersonalAccount;
import org.flexpay.ab.persistence.Apartment;

import java.util.List;

public interface PersonalAccountService {

	/**
	 * Get list of personal accounts for apartment
	 *
	 * @param apartment Apartment
	 * @return List of PersonalAccounts created for this apartment
	 */
	List<PersonalAccount> findAccounts(Apartment apartment);
}
