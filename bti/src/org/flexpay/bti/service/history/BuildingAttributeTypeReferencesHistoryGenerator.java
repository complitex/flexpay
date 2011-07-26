package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class BuildingAttributeTypeReferencesHistoryGenerator implements ReferencesHistoryGenerator<BuildingAttributeType> {

	private BuildingAttributeGroupHistoryGenerator groupHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull BuildingAttributeType obj) {
		groupHistoryGenerator.generateFor(obj.getGroup());
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
    }

	@Required
	public void setGroupHistoryGenerator(BuildingAttributeGroupHistoryGenerator groupHistoryGenerator) {
		this.groupHistoryGenerator = groupHistoryGenerator;
	}

}
