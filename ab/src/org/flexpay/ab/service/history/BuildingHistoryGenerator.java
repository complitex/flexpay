package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BuildingHistoryGenerator implements HistoryGenerator<Building> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private BuildingService buildingService;
	private DiffService diffService;

	private BuildingHistoryBuilder historyBuilder;
	private BuildingReferencesHistoryGenerator referencesHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Building obj) {

		log.debug("starting generating history for building #{}", obj.getId());

		// create building history
		Building building = buildingService.read(stub(obj));
		if (building == null) {
			log.warn("Building not found {}", building);
			return;
		}

		referencesHistoryGenerator.generateReferencesHistory(building);

		if (!diffService.hasDiffs(building)) {
			Diff diff = historyBuilder.diff(null, building);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		}
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setHistoryBuilder(BuildingHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setReferencesHistoryGenerator(BuildingReferencesHistoryGenerator referencesHistoryGenerator) {
		this.referencesHistoryGenerator = referencesHistoryGenerator;
	}
}
