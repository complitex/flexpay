package org.flexpay.ab.persistence.sorter;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.sorter.I18nObjectSorter;
import org.jetbrains.annotations.NotNull;

/**
 * Sorter that sorts countries by name
 */
public class CountrySorter extends I18nObjectSorter {

	protected String countryField;

	public CountrySorter() {
	}

	public CountrySorter(@NotNull Language lang) {
		super(lang);
	}

	public void setCountryField(String countryField) {
		this.countryField = countryField;
	}

	public void setFrom(StringBuilder query) {
		Language defaultLang = ApplicationConfig.getDefaultLanguage();
		query
				.append(" left join ").append(countryField).append(".countryNames ")
				.append(TRANSLATION_1).append(" with (" + TRANSLATION_1 + ".lang.id=").append(lang.getId()).append(")")
				.append(" left join ").append(countryField).append(".countryNames ")
				.append(TRANSLATION_2).append(" with (" + TRANSLATION_2 + ".lang.id=").append(defaultLang.getId()).append(") ");
	}
}
