package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.dao.paging.FetchRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Iterator;
import java.util.List;

/**
 * Generator of history of all persons
 */
public class PersonsHistoryGenerator {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private PersonService personService;
	private PersonHistoryGenerator historyGenerator;

	private int pageSize = 1000;

	/**
	 * Do generation
	 */
	public void generate() {

		log.debug("starting generating history for persons");

		FetchRange range = new FetchRange();
		range.setPageSize(pageSize);
		do {
			List<Person> persons = personService.listPersonsWithIdentities(range);
			List<Person> personRegistrations = personService.listPersonsWithRegistrations(range);
			Iterator<Person> regsIt = personRegistrations.iterator();
			Person lastRegistrations = regsIt.hasNext() ? regsIt.next() : null;
			for (Person person : persons) {
				// replace person registrations stub with a fetched set
				if (lastRegistrations != null) {
					if (person.equals(lastRegistrations)) {
						person.setPersonRegistrations(lastRegistrations.getPersonRegistrations());
						lastRegistrations = regsIt.hasNext() ? regsIt.next() : null;
					}
				}
				historyGenerator.generateFor(person);
			}
			range.nextPage();
		} while (range.hasMore());

		log.debug("Ended generating history for persons");
	}

    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        personService.setJpaTemplate(jpaTemplate);
        historyGenerator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setHistoryGenerator(PersonHistoryGenerator historyGenerator) {
		this.historyGenerator = historyGenerator;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
