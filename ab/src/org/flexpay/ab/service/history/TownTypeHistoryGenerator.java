package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownTypeHistoryGenerator implements HistoryGenerator<TownType> {

	private TownTypeService townTypeService;
	private DiffService diffService;

	private TownTypeHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull TownType obj) {

		obj = townTypeService.read(obj.getId());

		List<Diff> diffs = diffService.findDiffs(obj);
		if (!diffs.isEmpty()) {
			return;
		}

		Diff diff = historyBuilder.diff(null, obj);
		diffService.create(diff);
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(TownTypeHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
