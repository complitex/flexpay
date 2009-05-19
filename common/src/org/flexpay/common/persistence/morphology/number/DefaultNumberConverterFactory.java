package org.flexpay.common.persistence.morphology.number;

import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Locale;

public class DefaultNumberConverterFactory implements NumberConverterFactory {

	public NumberToTextConverter getInstance(Locale locale) {

		if (locale == null) {
			locale = ApplicationConfig.getDefaultLocale();
		}

		if ("EN".equals(locale.getCountry()) || "US".equals(locale.getCountry())) {
			return new EnNumberToTextConverter();
		}

		// default implementation is a russian one
		return new RuNumberToTextConverter();
	}
}
