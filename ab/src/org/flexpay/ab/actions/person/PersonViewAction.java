package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PersonViewAction extends FPActionSupport {

	private Person person = new Person();

	private PersonService personService;
	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        log.debug("Object: {}", person);

        if (person.isNew()) {
			addActionError(getText("error.no_id"));
			return SUCCESS;
		}

		person = personService.read(stub(person));
		if (person == null) {
			addActionError(getText("error.invalid_id"));
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

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getAddress() {
		try {
			Apartment registration = person.getRegistrationApartment();
			if (registration != null) {
				return apartmentService.getAddress(stub(registration));
			}

			log.warn("No registration for person: {}", person);
			return "";
		} catch (FlexPayException e) {
			return getErrorMessage(e);
		}
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

}
