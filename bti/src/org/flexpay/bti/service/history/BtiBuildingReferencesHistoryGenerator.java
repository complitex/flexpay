package org.flexpay.bti.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.history.BuildingReferencesHistoryGenerator;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BtiBuildingReferencesHistoryGenerator extends BuildingReferencesHistoryGenerator {

	private BuildingAttributeTypeHistoryGenerator attributeTypeHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Building obj) {
		super.generateReferencesHistory(obj);

		BtiBuilding btiBuilding = (BtiBuilding) obj;
		for (BuildingAttributeType type : btiBuilding.attributeTypes()) {
			attributeTypeHistoryGenerator.generateFor(type);
		}
	}

	@Required
	public void setBtiAttributeTypeHistoryGenerator(BuildingAttributeTypeHistoryGenerator attributeTypeHistoryGenerator) {
		this.attributeTypeHistoryGenerator = attributeTypeHistoryGenerator;
	}
}
