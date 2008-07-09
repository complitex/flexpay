package org.flexpay.ab.actions.person;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.service.IdentityTypeService;

public class SavePersonFIOAction extends FPActionSupport {

	private Person person = new Person();
	private PersonIdentity identity = new PersonIdentity();

	private PersonService personService;
	private IdentityTypeService identityTypeService;

	@Override
	public String doExecute() throws Exception {

		if (!isPost()) {
			return REDIRECT_SUCCESS;
		}

		if (person.isNotNew()) {
			person = personService.read(person);
		}

		if (log.isDebugEnabled()) {
			log.debug("FIO identity to save: " + identity);
		}

		identity.setIdentityType(identityTypeService.getType(IdentityType.TYPE_FIO));
		identity.setBeginDate(null);
		identity.setEndDate(null);
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");

		boolean modified = person.setFIOIdentity(identity);
		if (modified) {
			log.info("Saving person");
			personService.save(person);
			addActionError(getText("ab.person.fio.updated"));
			log.info("Saved, errors: " + getActionErrors());
		} else {
			log.info("Not modified");
		}

		return REDIRECT_SUCCESS;
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
		return REDIRECT_ERROR;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PersonIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(PersonIdentity identity) {
		this.identity = identity;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
