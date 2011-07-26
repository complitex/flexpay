package org.flexpay.ab.service.history;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class BuildingReferencesHistoryGeneratorHolder {

	private static BuildingReferencesHistoryGenerator generator;

	public BuildingReferencesHistoryGenerator getInstance() {
		return generator;
	}

    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        generator.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setReferencesHistoryGenerator(BuildingReferencesHistoryGenerator referencesHistoryGenerator) {
		BuildingReferencesHistoryGeneratorHolder.generator = referencesHistoryGenerator;
	}
}
