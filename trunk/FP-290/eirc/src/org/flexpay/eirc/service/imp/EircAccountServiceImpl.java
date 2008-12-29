package org.flexpay.eirc.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.SequenceService;
import org.flexpay.common.util.Luhn;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.dao.EircAccountDao;
import org.flexpay.eirc.dao.EircAccountDaoExt;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
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
	public EircAccount findAccount(@NotNull Stub<Person> personStub, @NotNull Stub<Apartment> apartmentStub) {
		return eircAccountDaoExt.findAccount(personStub.getId(), apartmentStub.getId());
	}

	/**
	 * Create or update account
	 *
	 * @param account EIRC account to save
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull EircAccount account) throws FlexPayExceptionContainer {
		validate(account);
		if (account.isNew()) {
			account.setId(null);
			account.setAccountNumber(nextPersonalAccount());
			Long id = eircAccountDao.create(account);
			account.setId(id);
		} else {
			eircAccountDao.update(account);
		}
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
	 * Find EircAccounts
	 *
	 * @param filters Filters stack
	 * @param pager   Accounts pager
	 * @return List of EircAccount
	 */
	public List<EircAccount> findAccounts(ArrayStack filters, Page<EircAccount> pager) {

		PersonSearchFilter personSearchFilter = null;
		if (filters.peek() instanceof PersonSearchFilter) {
			personSearchFilter = (PersonSearchFilter) filters.peek();
			if (personSearchFilter.needFilter()) {
				String str = "%" + personSearchFilter.getSearchString() + "%";
				return eircAccountDao.findByPersonFIO(str, pager);
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

	/**
	 * Read full account info, includes person and service
	 *
	 * @param stub Account stub
	 * @return EircAccount if found, or <code>null</code> if stub references no object
	 */
	@Nullable
	public EircAccount readFull(@NotNull Stub<EircAccount> stub) {
		return eircAccountDao.readFull(stub.getId());
	}
}
