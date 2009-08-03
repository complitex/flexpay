package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class EircAccountsListAjaxAction extends FPActionWithPagerSupport<EircAccount> {

	private String apartmentId;
	private String personFio;
	private List<EircAccount> accounts = list();

	private EircAccountService eircAccountService;
	private PersonService personService;
	private AddressService addressService;

	@Override
	@NotNull
	public String doExecute() throws Exception {

		Long apartmentIdLong = null;

		try {
			apartmentIdLong = Long.parseLong(apartmentId);
		} catch (Exception e) {
			log.warn("Incorrect apartment id in filter ({})", apartmentId);
		}

		accounts = eircAccountService.getAccounts(apartmentIdLong == null ? null : new Stub<Apartment>(apartmentIdLong),
				personFio, getPager());

		log.info("Found eirc accounts: {}", accounts);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
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

	public String getPersonFio() {
		return personFio;
	}

	public void setPersonFio(String personFio) {
		this.personFio = personFio;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
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
