package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RegionHistoryGenerator implements HistoryGenerator<Region> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private RegionService regionService;
	private DiffService diffService;

	private RegionHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	@Override
	public void generateFor(@NotNull Region obj) {

		log.debug("starting generating history for region #{}", obj.getId());

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
}
