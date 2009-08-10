package org.flexpay.ab.service.history;

import org.springframework.beans.factory.annotation.Required;

public class BuildingReferencesHistoryGeneratorHolder {

	private static BuildingReferencesHistoryGenerator generator;

	public BuildingReferencesHistoryGenerator getInstance() {
		return generator;
	}

	@Required
	public void setReferencesHistoryGenerator(BuildingReferencesHistoryGenerator referencesHistoryGenerator) {
		BuildingReferencesHistoryGeneratorHolder.generator = referencesHistoryGenerator;
	}
}
