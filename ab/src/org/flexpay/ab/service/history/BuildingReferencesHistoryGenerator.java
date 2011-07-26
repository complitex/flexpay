package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.AddressAttribute;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class BuildingReferencesHistoryGenerator implements ReferencesHistoryGenerator<Building> {

	private StreetHistoryGenerator streetHistoryGenerator;
	private DistrictHistoryGenerator districtHistoryGenerator;
	private AddressAttributeTypeHistoryGenerator attributeTypeHistoryGenerator;
	private DiffService diffService;

	@Override
	public void generateReferencesHistory(@NotNull Building obj) {

		if (!diffService.hasDiffs(obj.getDistrict())) {
			districtHistoryGenerator.generateFor(obj.getDistrict());
		}

		boolean allAttributeTypesHaveHistory = diffService.allObjectsHaveDiff(AddressAttributeType.class);

		for (BuildingAddress address : obj.getBuildingses()) {
			if (!diffService.hasDiffs(address.getStreet())) {
				streetHistoryGenerator.generateFor(address.getStreet());
			}

			if (!allAttributeTypesHaveHistory) {
				for (AddressAttribute attribute : address.getBuildingAttributes()) {
					attributeTypeHistoryGenerator.generateFor(attribute.getBuildingAttributeType());
				}
			}
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        streetHistoryGenerator.setJpaTemplate(jpaTemplate);
        districtHistoryGenerator.setJpaTemplate(jpaTemplate);
        attributeTypeHistoryGenerator.setJpaTemplate(jpaTemplate);
        diffService.setJpaTemplate(jpaTemplate);
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

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

}
