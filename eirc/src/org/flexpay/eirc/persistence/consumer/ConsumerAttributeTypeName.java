package org.flexpay.eirc.persistence.consumer;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

public class ConsumerAttributeTypeName extends Translation {

	public ConsumerAttributeTypeName() {
	}

	public ConsumerAttributeTypeName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	public ConsumerAttributeTypeName(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}
}
