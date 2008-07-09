package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;

public class EditPersonAction extends FPActionSupport {

	private PersonService personService;

	private Person person = new Person();

	public String doExecute() throws Exception {

		if (person.getId() == null) {
			log.info("No person id specified");
			addActionError(getText("error.ab.person.id_not_specified"));
			return REDIRECT_ERROR;
		}

		if (person.isNotNew()) {
			person = personService.read(person);
			if (person == null) {
				addActionError(getText("error.ab.person.invalid_id"));
				return REDIRECT_ERROR;
			}
		}

		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PersonIdentity getFIOIdentity() {
		PersonIdentity fio = person.getFIOIdentity();

		if (log.isDebugEnabled()) {
			log.debug("Person FIO: " + fio);
		}

		return fio != null ? fio : new PersonIdentity();
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
