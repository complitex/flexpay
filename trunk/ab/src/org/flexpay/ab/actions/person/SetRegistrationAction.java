package org.flexpay.ab.actions.person;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;

public class SetRegistrationAction implements SessionAware {
	public static final String PERSON_SESSION_KEY = "PERSON_SESSION_KEY";
	private PersonService personService;

	private Map session;
	private Long personId;
	private Long apartmentId;

	public String execute() {
		if (apartmentId == null || apartmentId.equals("")) {
			return "form";
		}
		personId = Long.parseLong((String) session.get(PERSON_SESSION_KEY));
		Person person = personService.read(personId);
		person.setApartment(new Apartment(apartmentId));
		personService.update(person);

		return "success";
	}

	/**
	 * @param apartmentId
	 *            the apartmentId to set
	 */
	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	/**
	 * @param personService
	 *            the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}
}
