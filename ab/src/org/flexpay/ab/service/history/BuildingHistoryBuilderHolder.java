package org.flexpay.ab.service.history;

import org.springframework.beans.factory.annotation.Required;

public class BuildingHistoryBuilderHolder {

	private static BuildingHistoryBuilder builder;

	public BuildingHistoryBuilder getInstance() {
		return builder;
	}

	@Required
	public void setBuilder(BuildingHistoryBuilder builder) {
		BuildingHistoryBuilderHolder.builder = builder;
	}
}
