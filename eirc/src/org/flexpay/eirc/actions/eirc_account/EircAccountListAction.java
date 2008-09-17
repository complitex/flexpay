package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.actions.apartment.ApartmentFilterDependent2Action;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.Page;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EircAccountListAction extends ApartmentFilterDependent2Action {

	private EircAccountService eircAccountService;
	private PersonService personService;
	private AddressService addressService;

	private List<EircAccount> eircAccountList;
	private Page<EircAccount> pager = new Page<EircAccount>();

	public EircAccountListAction() {
		apartmentFilter.setNeedAutoChange(true);
	}

	@NotNull
	public String doExecute() {

		initFilters();

		eircAccountList = eircAccountService.findAll(getFilters(), pager);


		return SUCCESS;
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
	protected String getErrorResult() {
		return SUCCESS;
	}

	public String getAddress(@NotNull Apartment apartment) throws Exception {
		return addressService.getAddress(stub(apartment), getLocale());
	}

	public String getFIO(@NotNull Person person) {
		Person persistent = personService.read(stub(person));
		if (persistent == null) {
			throw new RuntimeException("Invalid person: " + person);
		}
		PersonIdentity identity = persistent.getDefaultIdentity();
		if (identity != null) {
			return identity.getFirstName() + " " + identity.getMiddleName() + " " + identity.getLastName();
		}
		throw new RuntimeException("No default identity: " + persistent);
	}

	/**
	 * @return the pager
	 */
	public Page<EircAccount> getPager() {
		return pager;
	}

	/**
	 * @param pager the pager to set
	 */
	public void setPager(Page<EircAccount> pager) {
		this.pager = pager;
	}

	/**
	 * @return the eircAccountList
	 */
	public List<EircAccount> getEircAccountList() {
		return eircAccountList;
	}

	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
