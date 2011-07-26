package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class BuildingReferencesHistoryGeneratorProxy implements ReferencesHistoryGenerator<Building> {

	private BuildingReferencesHistoryGeneratorHolder historyGeneratorHolder;

	@Override
	public void generateReferencesHistory(@NotNull Building obj) {
		historyGeneratorHolder.getInstance().generateReferencesHistory(obj);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        historyGeneratorHolder.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setHistoryGeneratorHolder(BuildingReferencesHistoryGeneratorHolder historyGeneratorHolder) {
		this.historyGeneratorHolder = historyGeneratorHolder;
	}

}
