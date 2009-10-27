package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
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

		if (person.getId() == null) {
			log.info("No person id specified");
			addActionError(getText("error.ab.person.id_not_specified"));
			return REDIRECT_ERROR;
		}

		if (person.isNotNew()) {
			person = personService.read(stub(person));
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
	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public PersonIdentity getFIOIdentity() {
		PersonIdentity fio = person.getFIOIdentity();
		return fio != null ? fio : new PersonIdentity();
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
