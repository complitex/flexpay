package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class DistrictReferencesHistoryGenerator implements ReferencesHistoryGenerator<District> {

	private TownHistoryGenerator townHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull District obj) {
		townHistoryGenerator.generateFor(obj.getTown());
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        townHistoryGenerator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setTownHistoryGenerator(TownHistoryGenerator townHistoryGenerator) {
		this.townHistoryGenerator = townHistoryGenerator;
	}

}
