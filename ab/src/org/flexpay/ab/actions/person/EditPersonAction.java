package org.flexpay.ab.actions.person;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

public class EditPersonAction extends FPActionSupport {

	private PersonService personService;
	private IdentityTypeService identityTypeService;

	private Map<Long, PersonIdentity> identities = CollectionUtils.map();
	private Person person = new Person();

	public String execute() {

		Person persistent = personService.read(person);
		if (persistent == null) {
			persistent = person;
		}
		if (isSubmitted()) {
			for (PersonIdentity identity : identities.values()) {
				persistent.setIdentity(identity);
			}

			try {
				personService.save(persistent);
				return SUCCESS;
			} catch (FlexPayExceptionContainer container) {
				addActionErrors(container);
			}
		} else {
			person = persistent;
			initIdentities();
		}

		return INPUT;
	}

	private void initIdentities() {
		for (PersonIdentity identity : person.getPersonIdentities()) {
			identities.put(identity.getIdentityType().getId(), identity);
		}

		Collection<IdentityType> types = identityTypeService.getIdentityTypes();
		for (IdentityType type : types) {
			if (!identities.containsKey(type.getId())) {
				PersonIdentity identity = new PersonIdentity();
				identity.setIdentityType(type);
				identities.put(type.getId(), identity);
			}
		}
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Map<Long, PersonIdentity> getIdentities() {
		return identities;
	}

	public void setIdentities(Map<Long, PersonIdentity> identities) {
		this.identities = identities;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
