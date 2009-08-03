package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Person;
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

	/**
	 * Do generation
	 *
	 * @param person Object to generate history for
	 */
	public void generateFor(@NotNull Person person) {

		log.debug("starting generating history for person {}", person);

		// create apartment history
		Diff diff = historyBuilder.diff(null, person);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

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
}
