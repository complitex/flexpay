package org.flexpay.ab.persistence.sorter;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.sorter.I18nObjectSorter;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;
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

    @Override
	public void setFrom(StringBuilder query) {
		Language defaultLang = getDefaultLanguage();
		query
				.append(" left join ").append(countryField).append(".translations ")
				.append(TRANSLATION_1).append(" with (" + TRANSLATION_1 + ".lang.id=").append(lang.getId()).append(")")
				.append(" left join ").append(countryField).append(".translations ")
				.append(TRANSLATION_2).append(" with (" + TRANSLATION_2 + ".lang.id=").append(defaultLang.getId()).append(") ");
	}
}
