package org.flexpay.ab.service.history;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class BuildingHistoryBuilderHolder {

	private static BuildingHistoryBuilder builder;

	public BuildingHistoryBuilder getInstance() {
		return builder;
	}

    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        builder.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setBuilder(BuildingHistoryBuilder builder) {
		BuildingHistoryBuilderHolder.builder = builder;
	}
}
