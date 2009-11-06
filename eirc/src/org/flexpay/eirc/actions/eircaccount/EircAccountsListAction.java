package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class EircAccountsListAction extends FPActionWithPagerSupport<EircAccount> {

	private Long apartmentFilter;
	private Long buildingFilter;
	private String personFio;
	private List<EircAccount> accounts = list();

	private EircAccountService eircAccountService;
	private PersonService personService;
	private AddressService addressService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		boolean setApartment = apartmentFilter != null && apartmentFilter > 0;
		boolean setBuilding = buildingFilter != null && buildingFilter > 0;

		if (!setApartment && !setBuilding) {
			if (personFio != null) {
				return SUCCESS;
			} else {
				personFio = "";
			}
		}

		if (setApartment) {
			accounts = eircAccountService.getAccountsInApartment(new Stub<Apartment>(apartmentFilter), personFio, getPager());
		} else if (setBuilding) {
			accounts = eircAccountService.getAccountsInBuilding(new Stub<BuildingAddress>(buildingFilter), personFio, getPager());
		}

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

	public void setPersonFio(String personFio) {
		this.personFio = personFio;
	}

	public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

	public List<EircAccount> getAccounts() {
		return accounts;
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
