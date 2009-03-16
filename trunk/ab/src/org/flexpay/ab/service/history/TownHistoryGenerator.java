package org.flexpay.ab.service.history;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.service.TownTypeService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownHistoryGenerator implements HistoryGenerator<Town> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private TownTypeService townTypeService;
	private TownService townService;
	private DiffService diffService;

	private TownTypeHistoryGenerator townTypeHistoryGenerator;
	private TownHistoryBuilder historyBuilder;

	private StreetTypeService streetTypeService;
	private StreetTypeHistoryGenerator streetTypeHistoryGenerator;

	private DistrictService districtService;
	private DistrictHistoryGenerator districtHistoryGenerator;

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
			townTypeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for town types");

		log.debug("starting generating history for street types");
		// generate history for all street types
		for (StreetType type : streetTypeService.getEntities()) {
			streetTypeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for street types");

		// now create town history
		Town town = townService.readFull(stub(obj));
		if (town == null) {
			log.warn("Town not found {}", town);
			return;
		}

		List<Diff> diffs = diffService.findDiffs(town);
		if (diffs.isEmpty()) {
			log.debug("starting generating history for town {}", town);

			Diff diff = historyBuilder.diff(null, town);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);

			log.debug("Ended generating history for town {}", obj);
		} else {
			log.info("Town already has history, do nothing {}", town);
		}

		log.debug("starting generating history for districts");
		// generate history for all town districts
		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(town.getId()));
		for (District district : districtService.find(filters)) {
			districtHistoryGenerator.generateFor(district);
		}
		log.debug("ended generating history for districts");
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
	public void setTownTypeHistoryGenerator(TownTypeHistoryGenerator townTypeHistoryGenerator) {
		this.townTypeHistoryGenerator = townTypeHistoryGenerator;
	}

	@Required
	public void setHistoryBuilder(TownHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	@Required
	public void setStreetTypeHistoryGenerator(StreetTypeHistoryGenerator streetTypeHistoryGenerator) {
		this.streetTypeHistoryGenerator = streetTypeHistoryGenerator;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setDistrictHistoryGenerator(DistrictHistoryGenerator districtHistoryGenerator) {
		this.districtHistoryGenerator = districtHistoryGenerator;
	}
}
