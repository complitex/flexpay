package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentReferencesHistoryGenerator implements ReferencesHistoryGenerator<Apartment> {

	private BuildingHistoryGenerator buildingHistoryGenerator;
	private DiffService diffService;

	@Override
	public void generateReferencesHistory(@NotNull Apartment obj) {

		if (!diffService.hasDiffs(obj.getBuilding())) {
			buildingHistoryGenerator.generateFor(obj.getBuilding());
		}
	}

	@Required
	public void setBuildingHistoryGenerator(BuildingHistoryGenerator buildingHistoryGenerator) {
		this.buildingHistoryGenerator = buildingHistoryGenerator;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}
}
