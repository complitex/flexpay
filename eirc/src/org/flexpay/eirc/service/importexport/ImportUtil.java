package org.flexpay.eirc.service.importexport;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ImportUtil {

	private Logger log = LoggerFactory.getLogger(getClass());

	private PersonService personService;

	/**
	 * Lookup person in apartment
	 *
	 * @param apartmentStub Apartment stub to find person in
	 * @param fName		 First name
	 * @param mName		 Middle name
	 * @param lName		 Last name
	 * @param error		 Import error holder (error id will contain key if person not found)
	 * @return Person if found, or <code>null</code> otherwise
	 */
	@Nullable
	public Person findPersonByFIO(Stub<Apartment> apartmentStub, String fName, String mName, String lName, ImportError error) {

		List<Person> persons = personService.find(apartmentStub, new Page<Person>(10000));
		if (persons.isEmpty()) {
			log.debug("No registered persons found");
			error.setErrorId("eirc.error.import.person_no_registrants");
			return null;
		}

		// filter persons by FIO
		List<Person> candidates = CollectionUtils.list();
		for (Person person : persons) {
			for (PersonIdentity identity : person.getPersonIdentities()) {
				boolean sameFIO = new EqualsBuilder()
						.append(identity.getFirstName(), fName)
						.append(identity.getMiddleName(), mName)
						.append(identity.getLastName(), lName)
						.isEquals();
				if (sameFIO) {
					candidates.add(person);
				}
			}
		}

		if (candidates.size() == 1) {
			log.debug("Unique FIO match");
			return candidates.get(0);
		}

		if (candidates.size() > 1) {
			log.debug("Too many FIO matches");
			error.setErrorId("eirc.error.import.person_several_fio_match");
			return null;
		}

		log.debug("No FIO matches");
		error.setErrorId("eirc.error.import.person_no_fio_match");
		return null;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
