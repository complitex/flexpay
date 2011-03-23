package org.flexpay.ab.action.person;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class PersonEditPageAction extends FPActionSupport {

	private Person person = new Person();

	private String crumbCreateKey;
	private PersonService personService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (person == null || person.getId() == null) {
			log.warn("Incorrect person id");
			addActionError(getText("ab.error.person.incorrect_person_id"));
			return REDIRECT_ERROR;
		}

		if (person.isNotNew()) {
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
		if (person != null && person.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public PersonIdentity getFIOIdentity() {
		PersonIdentity fio = person.getFIOIdentity();
		return fio != null ? fio : new PersonIdentity();
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

    public short getSexMan() {
        return PersonIdentity.SEX_MAN;
    }

    public short getSexWoman() {
        return PersonIdentity.SEX_WOMAN;
    }

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
