package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class StreetTypeHistoryGenerator implements HistoryGenerator<StreetType> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private StreetTypeService streetTypeService;
	private DiffService diffService;

	private StreetTypeHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull StreetType obj) {

		if (diffService.hasDiffs(obj)) {
			log.info("Street type already has history, do nothing {}", obj);
			return;
		}

		obj = streetTypeService.read(stub(obj));

		Diff diff = historyBuilder.diff(null, obj);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(StreetTypeHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
