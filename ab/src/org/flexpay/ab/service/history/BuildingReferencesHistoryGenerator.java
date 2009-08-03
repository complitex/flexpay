package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.AddressAttribute;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingReferencesHistoryGenerator implements ReferencesHistoryGenerator<Building> {

	private StreetHistoryGenerator streetHistoryGenerator;
	private DistrictHistoryGenerator districtHistoryGenerator;
	private AddressAttributeTypeHistoryGenerator attributeTypeHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Building obj) {

		districtHistoryGenerator.generateFor(obj.getDistrict());

		for (BuildingAddress address : obj.getBuildingses()) {
			streetHistoryGenerator.generateFor(address.getStreet());

			for (AddressAttribute attribute : address.getBuildingAttributes()) {
				attributeTypeHistoryGenerator.generateFor(attribute.getBuildingAttributeType());
			}
		}
	}

	@Required
	public void setStreetHistoryGenerator(StreetHistoryGenerator streetHistoryGenerator) {
		this.streetHistoryGenerator = streetHistoryGenerator;
	}

	@Required
	public void setDistrictHistoryGenerator(DistrictHistoryGenerator districtHistoryGenerator) {
		this.districtHistoryGenerator = districtHistoryGenerator;
	}

	@Required
	public void setAttributeTypeHistoryGenerator(AddressAttributeTypeHistoryGenerator attributeTypeHistoryGenerator) {
		this.attributeTypeHistoryGenerator = attributeTypeHistoryGenerator;
	}
}
