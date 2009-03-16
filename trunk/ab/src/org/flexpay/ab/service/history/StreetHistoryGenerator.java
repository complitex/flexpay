package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class StreetHistoryGenerator implements HistoryGenerator<Street> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private StreetService streetService;
	private DiffService diffService;

	private StreetHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Street obj) {

		log.debug("starting generating history for district {}", obj);

		// create district history
		Street district = streetService.readFull(stub(obj));
		if (district == null) {
			log.warn("Town not found {}", district);
			return;
		}

		List<Diff> diffs = diffService.findDiffs(district);
		if (!diffs.isEmpty()) {
			log.info("Street already has history, do nothing {}", district);
			return;
		}

		Diff diff = historyBuilder.diff(null, district);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

		log.debug("Ended generating history for street {}", obj);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setHistoryBuilder(StreetHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
