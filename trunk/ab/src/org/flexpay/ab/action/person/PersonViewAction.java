package org.flexpay.ab.action.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class PersonViewAction extends FPActionSupport {

	private Person person = new Person();

	private PersonService personService;
	private AddressService addressService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        if (person == null || person.isNew()) {
			log.warn("Incorrect person id");
			addActionError(getText("ab.error.person.incorrect_person_id"));
			return REDIRECT_ERROR;
		}

		Stub<Person> stub = stub(person);
		person = personService.readFull(stub);

		if (person == null) {
			log.warn("Can't get person with id {} from DB", stub.getId());
			addActionError(getText("ab.error.person.cant_get_person"));
			return REDIRECT_ERROR;
		} else if (person.isNotActive()) {
			log.warn("Person with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.person.cant_get_person"));
			return REDIRECT_ERROR;
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
		return REDIRECT_ERROR;
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

	public void setPerson(Person person) {
		this.person = person;
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
