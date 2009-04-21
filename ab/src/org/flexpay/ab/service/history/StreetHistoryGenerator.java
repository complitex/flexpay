package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.StreetService;
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

import java.util.List;

public class StreetHistoryGenerator implements HistoryGenerator<Street> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private StreetService streetService;
	private DiffService diffService;

	private StreetHistoryBuilder historyBuilder;

	private BuildingService buildingService;
	private BuildingHistoryGenerator buildingHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Street obj) {

		log.debug("starting generating history for street {}", obj);

		// create street history
		Street street = streetService.readFull(stub(obj));
		if (street == null) {
			log.warn("Street not found {}", obj);
			return;
		}

		if (!diffService.hasDiffs(street)) {
			Diff diff = historyBuilder.diff(null, street);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);
		} else {
			log.info("Street already has history, do nothing {}", street);
		}
		log.debug("Ended generating history for street {}", obj);

		log.debug("starting generating history for street buildings {}", obj);
		List<Building> buidings = buildingService.findStreetBuildings(stub(obj));
		for (Building building : buidings) {
			// generate history for this building if it has primary address on this street
			// done in order to prevent duplicates appear
			BuildingAddress address = building.getAddressOnStreet(street);
			if (address != null && address.isPrimary()) {
				buildingHistoryGenerator.generateFor(building);
			}
		}
		log.debug("Ended generating history for street buildings {}", obj);
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

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setBuildingHistoryGenerator(BuildingHistoryGenerator buildingHistoryGenerator) {
		this.buildingHistoryGenerator = buildingHistoryGenerator;
	}
}
