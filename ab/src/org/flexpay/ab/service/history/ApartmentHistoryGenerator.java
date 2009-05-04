package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentHistoryGenerator implements HistoryGenerator<Apartment> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;

	private ApartmentHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param apartment Object to generate history for
	 */
	public void generateFor(@NotNull Apartment apartment) {

		log.debug("starting generating history for apartment {}", apartment);

		// create apartment history
		Diff diff = historyBuilder.diff(null, apartment);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

		log.debug("Ended generating history for apartment {}", apartment);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(ApartmentHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
