package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;

public class ViewPerson extends FPActionSupport {

	private PersonService personService;
	private ApartmentService apartmentService;

	private Person person = new Person();

	public String doExecute() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Object: " + person);
		}
		if (person.isNotNew()) {
			person = personService.read(stub(person));
			if (person == null) {
				addActionError(getText("error.invalid_id"));
			}

			return SUCCESS;
		} else {
			addActionError(getText("error.no_id"));
			return SUCCESS;
		}
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	/**
	 * Getter for property 'person'.
	 *
	 * @return Value for property 'person'.
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * Setter for property 'person'.
	 *
	 * @param person Value to set for property 'person'.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * Setter for property 'personService'.
	 *
	 * @param personService Value to set for property 'personService'.
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @param apartmentService the apartmentService to set
	 */
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		try {
			Apartment registration = person.getRegistrationApartment();
			if (registration != null) {
				return apartmentService.getAddress(stub(registration));
			}

			log.warn("No registration for person: " + person);
			return "";
		} catch (FlexPayException e) {
			return getErrorMessage(e);
		}
	}
}
