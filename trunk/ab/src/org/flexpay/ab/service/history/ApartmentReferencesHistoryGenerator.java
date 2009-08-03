package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentReferencesHistoryGenerator implements ReferencesHistoryGenerator<Apartment> {

	private BuildingHistoryGenerator buildingHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Apartment obj) {

		buildingHistoryGenerator.generateFor(obj.getBuilding());
	}

	@Required
	public void setBuildingHistoryGenerator(BuildingHistoryGenerator buildingHistoryGenerator) {
		this.buildingHistoryGenerator = buildingHistoryGenerator;
	}
}
