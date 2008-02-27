package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.PersonalAccount;

import java.util.List;

/**
 * Personal account dao
 */
public interface PersonalAccountDao extends GenericDao<PersonalAccount, Long> {

	/**
	 * Find active personal accounts created for appertment
	 *
	 * @param apartmentId Apartment id
	 * @return List of personal accounts
	 */
	List<PersonalAccount> findAccounts(Long apartmentId);
}
