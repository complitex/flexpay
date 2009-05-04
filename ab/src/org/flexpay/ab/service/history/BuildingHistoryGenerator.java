package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.ApartmentService;
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

public class BuildingHistoryGenerator implements HistoryGenerator<Building> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private DiffService diffService;

	private ApartmentHistoryGenerator apartmentHistoryGenerator;
	private BuildingHistoryBuilder historyBuilder;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Building obj) {

		log.debug("starting generating history for building {}", obj);

		// create building history
		Building building = buildingService.read(stub(obj));
		if (building == null) {
			log.warn("Building not found {}", building);
			return;
		}

		Diff diff = historyBuilder.diff(null, building);
		diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
		diffService.create(diff);

		log.debug("Ended generating history for building {}", obj);

		// Commented untill perfomance issues fix
		log.debug("starting generating history for building apartments {}", obj);
		List<Apartment> apartments = apartmentService.getBuildingApartments(stub(obj));
		for (Apartment apartment : apartments) {
			// generate history for this apartment
			apartmentHistoryGenerator.generateFor(apartment);
		}
		log.debug("Ended generating history for building apartments {}", obj);
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
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setApartmentHistoryGenerator(ApartmentHistoryGenerator apartmentHistoryGenerator) {
		this.apartmentHistoryGenerator = apartmentHistoryGenerator;
	}
}
