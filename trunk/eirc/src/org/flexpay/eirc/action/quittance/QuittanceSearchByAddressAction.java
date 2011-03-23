package org.flexpay.eirc.action.quittance;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.action.apartment.ApartmentFilterDependent2Action;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class QuittanceSearchByAddressAction extends ApartmentFilterDependent2Action<EircAccount> {

	private Quittance quittance;
	private QuittanceService quittanceService;

	protected List<EircAccount> eircAccounts;

	private PersonSearchFilter personSearchFilter = new PersonSearchFilter();

	private EircAccountService eircAccountService;
	private PersonService personService;
	private AddressService addressService;

	public QuittanceSearchByAddressAction() {
		buildingsFilter.setAllowEmpty(true);
		apartmentFilter.setAllowEmpty(true);
		apartmentFilter.setNeedAutoChange(true);
		streetNameFilter.setShowSearchString(true);
	}

	@NotNull
	@Override
	public String doExecute() {

		initFilters();

		eircAccounts = eircAccountService.findAccounts(getFilters(), getPager());

		if (eircAccounts.size() != 1) {
			return SUCCESS;
		}

		EircAccount account = eircAccounts.get(0);
		quittance = getAccountQuittance(account.getId());
		if (quittance == null) {
			addActionError(getText("eirc.error.quittance.no_for_single_account"));
			return SUCCESS;
		}

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
    @Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	@Override
	public ArrayStack getFilters() {
		ArrayStack filters = super.getFilters();
		filters.push(personSearchFilter);

		return filters;
	}

	@Override
	public void setFilters(ArrayStack filters) {
		setFilters(filters, 7);
	}

	@Override
	protected int setFilters(ArrayStack filters, int n) {
		n = super.setFilters(filters, n);
		personSearchFilter = (PersonSearchFilter) filters.peek(--n);

		return n;
	}

	public Quittance getAccountQuittance(Long accountId) {

		List<Quittance> quittances = quittanceService.getLatestAccountQuittances(new EircAccount(accountId));
		if (quittances.isEmpty()) {
			return null;
		}

		return quittances.get(0);
	}

	public String getAddress(@NotNull Apartment apartment) throws Exception {
		return addressService.getAddress(stub(apartment), getLocale());
	}

	public String getFIO(@NotNull Person person) {
		Person persistent = personService.readFull(stub(person));
		if (persistent == null) {
			throw new RuntimeException("Invalid person: " + person);
		}
		PersonIdentity identity = persistent.getDefaultIdentity();
		if (identity != null) {
			return identity.getLastName() + " " + identity.getFirstName() + " " + identity.getMiddleName();
		}
		throw new RuntimeException("No default identity: " + persistent);
	}

	@Override
	protected boolean ignoreFilterInitErrors() {
		return true;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	public List<EircAccount> getEircAccounts() {
		return eircAccounts;
	}

	public PersonSearchFilter getPersonSearchFilter() {
		return personSearchFilter;
	}

	public void setPersonSearchFilter(PersonSearchFilter personSearchFilter) {
		this.personSearchFilter = personSearchFilter;
	}

	@Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

}
