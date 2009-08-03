package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class TownHistoryGenerator implements HistoryGenerator<Town> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private TownService townService;
	private DiffService diffService;

	private TownHistoryBuilder historyBuilder;
	private TownReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Town obj) {

		log.debug("starting generating history for town #{}", obj.getId());

		// now create town history
		Town town = townService.readFull(stub(obj));
		if (town == null) {
			log.warn("Town not found {}", town);
			return;
		}

		referencesHistoryGenerator.generateReferencesHistory(town);

		if (!diffService.hasDiffs(town)) {
			log.debug("starting generating history for town {}", town);

			Diff diff = historyBuilder.diff(null, town);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);

			log.debug("Ended generating history for town {}", town);
		} else {
			log.info("Town already has history, do nothing {}", town);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setHistoryBuilder(TownHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(TownReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
