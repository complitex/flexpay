package org.flexpay.bti.persistence.building;

import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

public class BuildingAttributeTypeName extends Translation {

	public BuildingAttributeTypeName() {
	}

	public BuildingAttributeTypeName(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public BuildingAttributeTypeName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
