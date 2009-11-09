package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PersonHistoryGenerator implements HistoryGenerator<Person> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;

	private PersonHistoryBuilder historyBuilder;
	private PersonService personService;
	private PersonReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull Person obj) {

		log.debug("starting generating history for person #{}", obj.getId());

		// create apartment history
		Person person = personService.readFull(stub(obj));
		if (person == null) {
			log.warn("Person not found", obj);
			return;
		}

		referencesHistoryGenerator.generateReferencesHistory(person);

		if (!diffService.hasDiffs(person)) {
			Diff diff = historyBuilder.diff(null, person);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}

		log.debug("Ended generating history for person {}", person);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(PersonHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Required
	public void setReferencesHistoryGenerator(PersonReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
