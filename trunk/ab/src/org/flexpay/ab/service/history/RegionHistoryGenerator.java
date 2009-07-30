package org.flexpay.ab.service.history;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.*;
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

public class RegionHistoryGenerator implements HistoryGenerator<Region> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private RegionService regionService;
	private DiffService diffService;

	private RegionHistoryBuilder historyBuilder;

	private TownService townService;
	private TownHistoryGenerator townHistoryGenerator;

	private TownTypeService townTypeService;
	private TownTypeHistoryGenerator townTypeHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Region obj) {

		log.debug("starting generating history for region {}", obj);

		// create region history
		Region region = regionService.readFull(stub(obj));
		if (region == null) {
			log.warn("Region not found {}", region);
			return;
		}

		if (!diffService.hasDiffs(region)) {
			Diff diff = historyBuilder.diff(null, region);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		} else {
			log.info("Region already has history, do nothing {}", region);
		}
		log.debug("Ended generating history for region {}", obj);


		log.debug("starting generating history for town types");
		// generate history for all town types
		for (TownType type : townTypeService.getEntities()) {
			townTypeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for town types");

		log.debug("starting generating history for towns");
		// generate history for all town districts
		ArrayStack filters = CollectionUtils.arrayStack(new RegionFilter(region.getId()));
		for (Town town : townService.find(filters)) {
			townHistoryGenerator.generateFor(town);
		}
		log.debug("ended generating history for towns");

	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setHistoryBuilder(RegionHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setTownHistoryGenerator(TownHistoryGenerator townHistoryGenerator) {
		this.townHistoryGenerator = townHistoryGenerator;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	@Required
	public void setTownTypeHistoryGenerator(TownTypeHistoryGenerator townTypeHistoryGenerator) {
		this.townTypeHistoryGenerator = townTypeHistoryGenerator;
	}

}
