package org.flexpay.orgs.service.history;

import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SubdivisionHistoryGenerator implements HistoryGenerator<Subdivision> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private DiffService diffService;
	private SubdivisionHistoryBuilder historyBuilder;
	private SubdivisionService subdivisionService;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Subdivision obj) {

		if (diffService.hasDiffs(obj)) {
			log.debug("Subdivision already has history, do nothing {}", obj);
			return;
		}

		Subdivision subdivision = subdivisionService.read(stub(obj));
		if (subdivision == null) {
			log.warn("Requested subdivision history generation, but not found: {}", obj);
			return;
		}

		Diff diff = historyBuilder.diff(null, subdivision);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}

	@Required
	public void setHistoryBuilder(SubdivisionHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
