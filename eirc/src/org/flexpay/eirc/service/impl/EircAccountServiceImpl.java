package org.flexpay.eirc.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.service.SequenceService;
import org.flexpay.common.util.Luhn;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.dao.EircAccountDao;
import org.flexpay.eirc.dao.EircAccountDaoExt;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.sorter.EircAccountSorter;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional (readOnly = true)
public class EircAccountServiceImpl implements EircAccountService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private EircAccountDaoExt eircAccountDaoExt;
	private EircAccountDao eircAccountDao;

	private PersonService personService;
	private SequenceService sequenceService;

	/**
	 * Find EircAccount by person and apartment references
	 *
	 * @param personStub	Person reference
	 * @param apartmentStub Apartment reference
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	@Override
	public EircAccount findAccount(@NotNull Stub<Person> personStub, @NotNull Stub<Apartment> apartmentStub) {
		return eircAccountDaoExt.findAccount(personStub.getId(), apartmentStub.getId());
	}

	/**
	 * Find EircAccount by apartment references
	 *
	 * @param apartmentStub Apartment reference
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	@Override
	public EircAccount findAccount(@NotNull Stub<Apartment> apartmentStub) {
		return eircAccountDaoExt.findAccount(apartmentStub.getId());
	}

	/**
	 * Create account
	 *
	 * @param account EIRC account to save
	 */
	@NotNull
	@Override
	@Transactional (readOnly = false)
	public EircAccount create(@NotNull EircAccount account) throws FlexPayExceptionContainer {
		validate(account);
		account.setId(null);
		account.setAccountNumber(nextPersonalAccount());
		eircAccountDao.create(account);

		return account;
	}

	/**
	 * Update account
	 *
	 * @param account EIRC account to save
	 */
	@NotNull
	@Override
	@Transactional (readOnly = false)
	public EircAccount update(@NotNull EircAccount account) throws FlexPayExceptionContainer {

		validate(account);
		eircAccountDao.update(account);

		return account;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(EircAccount account) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		Stub<Person> personStub = account.getPersonStub();
		if (account.isNew() && personStub != null) {
			EircAccount persistent = findAccount(personStub, account.getApartmentStub());
			if (persistent != null) {
				ex.addException(new FlexPayException("Duplicate account", "eirc.error.account.create.duplicate"));
			}
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Transactional (readOnly = false)
	public String nextPersonalAccount() {
		String eircId = ApplicationConfig.getEircId();
		String result = sequenceService.next(
				SequenceService.PERSONAL_ACCOUNT_SEQUENCE_ID).toString();
		result = StringUtil.fillLeadingZero(result, 7);
		result = eircId + result;
		result = Luhn.insertControlDigit(result, 1);
		return result;
	}

	/**
	 * Find EircAccounts
	 *
	 * @param filters Filters stack
	 * @param pager   Accounts pager
	 * @return List of EircAccount
	 */
	@Override
	public List<EircAccount> findAccounts(ArrayStack filters, Page<EircAccount> pager) {

		PersonSearchFilter personSearchFilter = null;
		if (filters.peek() instanceof PersonSearchFilter) {
			personSearchFilter = (PersonSearchFilter) filters.peek();
			if (personSearchFilter.needFilter()) {
				String str = "%" + personSearchFilter.getSearchString() + "%";
				return eircAccountDao.findByPersonFIO(str, str, pager);
			}
			personSearchFilter = (PersonSearchFilter) filters.pop();
		}

		if (filters.peek() instanceof ApartmentFilter) {
			ApartmentFilter filter = (ApartmentFilter) filters.peek();
			if (filter.needFilter()) {
				if (personSearchFilter != null) {
					filters.push(personSearchFilter);
				}
				return eircAccountDao.findByApartment(filter.getSelectedId(), pager);
			}
		}
		if (personSearchFilter != null) {
			filters.push(personSearchFilter);
		}

		return eircAccountDao.findObjects(pager);
	}

	@Override
	public List<EircAccount> getAccounts(@NotNull List<? extends EircAccountSorter> sorters, Collection<ObjectFilter> filters, @NotNull Integer output, Page<EircAccount> pager) {
        return eircAccountDaoExt.findAccounts(sorters, filters, output, pager);
	}

	/**
	 * Find account by its number
	 *
	 * @param accountNumber EircAccount number to lookup
	 * @return EircAccount if found, or <code>null</code> otherwise
	 */
	@Override
	public EircAccount findAccount(String accountNumber) {
		List<EircAccount> accounts = eircAccountDao.findByNumber(accountNumber);
		if (accounts.size() > 1) {
			throw new RuntimeException("Internal error, account number duplicate: " + accountNumber);
		}

		return accounts.isEmpty() ? null : accounts.get(0);
	}

	/**
	 * Read full account info, includes person and service
	 *
	 * @param stub Account stub
	 * @return EircAccount if found, or <code>null</code> if stub references no object
	 */
	@Nullable
	@Override
	public EircAccount readFull(@NotNull Stub<EircAccount> stub) {
		return eircAccountDao.readFull(stub.getId());
	}

	/**
	 * Get person FIO that account was created for
	 *
	 * @param account EircAccount to get person last-first-middle names for
	 * @return person last-first-middle names if found, or <code>null</code> otherwise
	 */
	@Override
	public String getPersonFIO(@NotNull EircAccount account) {

		Stub<Person> personStub = account.getPersonStub();
		if (personStub == null) {
			return account.getConsumerInfo().getFIO();
		}

		Person person = personService.readFull(personStub);
		if (person == null) {
			log.error("No person found {}", personStub);
			return null;
		}

		return person.getFIO();
	}

	@Required
	public void setEircAccountDaoExt(EircAccountDaoExt eircAccountDaoExt) {
		this.eircAccountDaoExt = eircAccountDaoExt;
	}

	@Required
	public void setEircAccountDao(EircAccountDao eircAccountDao) {
		this.eircAccountDao = eircAccountDao;
	}

	@Required
	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
