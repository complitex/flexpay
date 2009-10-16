package org.flexpay.bti.persistence.building;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;
import org.jetbrains.annotations.NotNull;

public class BuildingAttributeTypeName extends Translation {

	public BuildingAttributeTypeName() {
	}

	public BuildingAttributeTypeName(@NotNull String name) {
		super(name, getDefaultLanguage());
	}

	public BuildingAttributeTypeName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
