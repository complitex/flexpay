package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PersonViewIdentitiesAction extends FPActionSupport {

	private Person person = new Person();

	private PersonService personService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (person == null || person.isNew()) {
			log.debug("Incorrect person id");
			return SUCCESS;
		}

		Stub<Person> stub = stub(person);
		person = personService.readFull(stub);

		if (person == null) {
			log.debug("Can't get person with id {} from DB", stub.getId());
		} else if (person.isNotActive()) {
			log.debug("Person with id {} is disabled", stub.getId());
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

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
