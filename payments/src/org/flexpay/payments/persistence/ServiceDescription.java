package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

public class ServiceDescription extends Translation {

	public ServiceDescription() {
		
	}

	public ServiceDescription(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public ServiceDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
