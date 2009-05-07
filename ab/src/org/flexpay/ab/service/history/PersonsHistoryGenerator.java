package org.flexpay.ab.service.history;

import org.flexpay.ab.service.PersonService;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.FetchRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

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
			for (Person person : persons) {
				historyGenerator.generateFor(person);
			}
		} while (range.hasMore());

		log.debug("Ended generating history for persons");
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
