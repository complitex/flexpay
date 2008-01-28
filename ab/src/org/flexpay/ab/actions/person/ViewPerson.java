package org.flexpay.ab.actions.person;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.apache.log4j.Logger;

public class ViewPerson extends FPActionSupport {

	private static Logger log = Logger.getLogger(ViewPerson.class);

	private PersonService personService;

	private Person person = new Person();

	@Override
	public String execute() throws Exception {
		log.info("Object: " + person);
		if (person.getId() != null) {
			person = personService.read(person.getId());
			return SUCCESS;
		} else {
			addActionError(getText("error.no_id"));
			return ERROR;
		}
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
}
