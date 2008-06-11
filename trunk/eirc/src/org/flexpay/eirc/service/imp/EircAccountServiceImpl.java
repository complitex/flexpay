package org.flexpay.eirc.service.imp;

import java.util.List;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.SequenceService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.Luhn;
import org.flexpay.eirc.dao.EircAccountDao;
import org.flexpay.eirc.dao.EircAccountDaoExt;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class EircAccountServiceImpl implements EircAccountService {

	private EircAccountDaoExt eircAccountDaoExt;
	private EircAccountDao eircAccountDao;

	private SequenceService sequenceService;

	/**
	 * Find EircAccount by person and apartment references
	 *
	 * @param personStub	Person reference
	 * @param apartmentStub Apartment reference
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	public EircAccount findAccount(Person personStub, Apartment apartmentStub) {
		return eircAccountDaoExt.findAccount(personStub.getId(), apartmentStub.getId());
	}

	/**
	 * Create or update account
	 *
	 * @param account EIRC account to save
	 */
	@Transactional(readOnly = false)
	public void save(EircAccount account) throws FlexPayExceptionContainer {
		if (account.isNew()) {
			account.setId(null);
			account.setAccountNumber(nextPersonalAccount());
			eircAccountDao.create(account);
		} else {
			eircAccountDao.update(account);
		}
	}

	@Transactional(readOnly = false)
	public String nextPersonalAccount() {
		String eircId = ApplicationConfig.getInstance().getEircId();
		String result = sequenceService.next(
				SequenceService.PERSONAL_ACCOUNT_SEQUENCE_ID).toString();
		result = StringUtil.fillLeadingZero(result, 7);
		result = eircId + result;
		result = Luhn.insertControlDigit(result, 1);
		return result;
	}

	public void setEircAccountDaoExt(EircAccountDaoExt eircAccountDaoExt) {
		this.eircAccountDaoExt = eircAccountDaoExt;
	}

	public void setEircAccountDao(EircAccountDao eircAccountDao) {
		this.eircAccountDao = eircAccountDao;
	}

	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
	
	/**
	 * Find all EircAccounts
	 *
	 * @return List of EircAccount
	 */
	public List<EircAccount> findAll(Page<EircAccount> pager) {
		return this.eircAccountDao.findObjects(pager);
		
	}
	
	public EircAccount findWithPerson(Long id) {
		List<EircAccount> list = eircAccountDao.findWithPerson(id);
		if(list.isEmpty()) {
			return null;
		}
		
		return list.iterator().next();
	}
}
