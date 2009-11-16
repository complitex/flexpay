package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PersonEditPageAction extends FPActionSupport {

	private Person person = new Person();

	private String crumbCreateKey;
	private PersonService personService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		person = person.isNew() ? person : personService.readFull(stub(person));

		if (person == null) {
			addActionError(getText("ab.error.person.invalid_id"));
			return REDIRECT_ERROR;
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
	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	@Override
	protected void setBreadCrumbs() {
		if (person.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
