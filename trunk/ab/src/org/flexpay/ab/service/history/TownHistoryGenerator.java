package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.Diff;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TownHistoryGenerator implements HistoryGenerator<Town> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private TownTypeService townTypeService;
	private TownService townService;
	private DiffService diffService;

	private TownTypeHistoryGenerator typeHistoryGenerator;
	private TownHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Town obj) {

		log.debug("starting generating history for town {}", obj);

		log.debug("starting generating history for town types");
		// generate history for all town types
		for (TownType type : townTypeService.getEntities()) {
			typeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for town types");

		// now create town history
		Town town = townService.readFull(stub(obj));
		if (town == null) {
			log.warn("Town not found {}", town);
			return;
		}

		List<Diff> diffs = diffService.findDiffs(town);
		if (!diffs.isEmpty()) {
			log.info("Town already has history, do nothing {}", town);
			return;
		}

		Diff diff = historyBuilder.diff(null, town);
		diffService.create(diff);

		log.debug("Ended generating history for town {}", obj);
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
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setTypeHistoryGenerator(TownTypeHistoryGenerator typeHistoryGenerator) {
		this.typeHistoryGenerator = typeHistoryGenerator;
	}

	@Required
	public void setHistoryBuilder(TownHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}
}
