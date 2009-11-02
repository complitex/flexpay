package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PersonSaveFIOAction extends FPActionSupport {

	private Person person = new Person();
	private PersonIdentity identity = new PersonIdentity();

	private PersonService personService;
	private IdentityTypeService identityTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (person.isNotNew()) {
			person = personService.read(stub(person));
		}

		log.debug("FIO identity to save: {}", identity);

		identity.setIdentityType(identityTypeService.findTypeById(IdentityType.TYPE_FIO));
		identity.setBeginDate(null);
		identity.setEndDate(null);
		identity.setOrganization("");
		identity.setSerialNumber("");
		identity.setDocumentNumber("");

		// set default identity flag to true if person is new 
		if (person.isNew()) {
			identity.setDefault(true);
		} else {
			identity.setDefault(person.getFIOIdentity().isDefault());
		}

		boolean modified = person.setFIOIdentity(identity);
		if (modified) {
			log.info("Saving person");
			if (person.isNew()) {
				personService.create(person);
			} else {
				personService.update(person);
			}
			addActionMessage(getText("ab.person.fio.saved"));
		} else {
			log.info("Not modified");
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

	public PersonIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(PersonIdentity identity) {
		this.identity = identity;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

}
