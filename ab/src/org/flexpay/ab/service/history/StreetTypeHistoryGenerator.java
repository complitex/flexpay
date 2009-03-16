package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class StreetTypeHistoryGenerator implements HistoryGenerator<StreetType> {

	private StreetTypeService streetTypeService;
	private DiffService diffService;

	private StreetTypeHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull StreetType obj) {

		obj = streetTypeService.read(obj.getId());

		List<Diff> diffs = diffService.findDiffs(obj);
		if (!diffs.isEmpty()) {
			return;
		}

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
