package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.PersonalAccountService;
import org.flexpay.eirc.persistence.PersonalAccount;
import org.flexpay.eirc.dao.PersonalAccountDao;
import org.flexpay.ab.persistence.Apartment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class PersonalAccountServiceImpl implements PersonalAccountService {

	private PersonalAccountDao personalAccountDao;

	public void setPersonalAccountDao(PersonalAccountDao personalAccountDao) {
		this.personalAccountDao = personalAccountDao;
	}

	/**
	 * Get list of personal accounts for apartment
	 *
	 * @param apartment Apartment
	 * @return List of PersonalAccounts created for this apartment
	 */
	public List<PersonalAccount> findAccounts(Apartment apartment) {
		return personalAccountDao.findAccounts(apartment.getId());
	}
}
