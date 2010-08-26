package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.sorter.EircAccountSorter;
import org.flexpay.eirc.persistence.sorter.EircAccountSorterByAccountNumber;
import org.flexpay.eirc.persistence.sorter.EircAccountSorterByAddress;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

public class EircAccountsListAction extends FPActionWithPagerSupport<EircAccount> {

	private Long apartmentFilter;
	private Long buildingFilter;
    private PersonSearchFilter personSearchFilter = new PersonSearchFilter();
    private Integer output = 1;
	private List<EircAccount> accounts = list();

    private EircAccountSorterByAccountNumber eircAccountSorterByAccountNumber = new EircAccountSorterByAccountNumber();
    private EircAccountSorterByAddress eircAccountSorterByAddress = new EircAccountSorterByAddress();

	private EircAccountService eircAccountService;
	private PersonService personService;
	private AddressService addressService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        if (personSearchFilter == null) {
            log.warn("PersonSearchFilter parameter is null");
            personSearchFilter = new PersonSearchFilter();
        }

        log.debug("apartmentFilter = {}, buildingFilter = {}", apartmentFilter, buildingFilter);
        log.debug("personSearchFilter = {}, output = {}", personSearchFilter, output);

        List<EircAccountSorter> sorters = list(eircAccountSorterByAccountNumber, eircAccountSorterByAddress);

		if ((apartmentFilter == null || apartmentFilter <= 0) && (buildingFilter == null || buildingFilter <= 0) && isEmpty(personSearchFilter.getSearchString())) {
            log.debug("All filters are empty. Return empty list");
            return SUCCESS;
		}

        List<ObjectFilter> filters = list(
                new ApartmentFilter(apartmentFilter),
                new BuildingsFilter(buildingFilter),
                personSearchFilter
        );

        accounts = eircAccountService.getAccounts(sorters, filters, output, getPager());

		log.debug("Found eirc accounts: {}", accounts);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
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

    public void setPersonSearchFilter(PersonSearchFilter personSearchFilter) {
        this.personSearchFilter = personSearchFilter;
    }

    public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

    public void setOutput(Integer output) {
        this.output = output;
    }

    public List<EircAccount> getAccounts() {
		return accounts;
	}

    public EircAccountSorterByAccountNumber getEircAccountSorterByAccountNumber() {
        return eircAccountSorterByAccountNumber;
    }

    public void setEircAccountSorterByAccountNumber(EircAccountSorterByAccountNumber eircAccountSorterByAccountNumber) {
        this.eircAccountSorterByAccountNumber = eircAccountSorterByAccountNumber;
    }

    public EircAccountSorterByAddress getEircAccountSorterByAddress() {
        return eircAccountSorterByAddress;
    }

    public void setEircAccountSorterByAddress(EircAccountSorterByAddress eircAccountSorterByAddress) {
        this.eircAccountSorterByAddress = eircAccountSorterByAddress;
    }

    @Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

}
