package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PersonViewAction extends FPActionSupport {

	private Person person = new Person();

	private PersonService personService;
	private AddressService addressService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        if (person.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		person = personService.readFull(stub(person));
		if (person == null) {
			addActionError(getText("common.error.invalid_id"));
		}

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

	public Person getPerson() {
		return person;
	}

	public String getAddress() {
		try {
			Apartment registration = person.getRegistrationApartment();
			if (registration != null) {
				return addressService.getAddress(stub(registration), getLocale());
			}

			log.warn("No registration for person: {}", person);
			return "";
		} catch (FlexPayException e) {
			return getErrorMessage(e);
		} catch (Exception e) {
			log.error("Error", e);
			return "";
		}
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
