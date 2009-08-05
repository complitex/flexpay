package org.flexpay.ab.persistence.sorter;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

/**
 * Sorter that sorts streets by name
 */
public class StreetSorterByName extends StreetSorter {

	public StreetSorterByName() {
	}

	public StreetSorterByName(@NotNull Language lang) {
		super(lang);
	}

	@Override
	public void setFrom(StringBuilder query) {
		Language defaultLang = ApplicationConfig.getDefaultLanguage();
		query
				.append(" left join ").append(streetField).append(".nameTemporals sortNameTemporal ")
				.append(" left join sortNameTemporal.value sortNameTemporalValue ")
				.append(" left outer join sortNameTemporalValue.translations ")
				.append(TRANSLATION_1).append(" with (" + TRANSLATION_1 + ".lang.id=").append(lang.getId()).append(")")
				.append(" left outer join sortNameTemporalValue.translations ")
				.append(TRANSLATION_2).append(" with (" + TRANSLATION_2 + ".lang.id=").append(defaultLang.getId()).append(") ");
	}

	/**
	 * Add HQL addendum for WHERE clause
	 *
	 * @param query HQL query to update
	 */
	@Override
	public void setWhere(StringBuilder query) {

		if (query.length() > 0) {
			query.append(" and ");
		}

		query.append(" (")
				.append("sortNameTemporal.invalidDate='2100-12-31' and ")
				.append("sortNameTemporal.begin <= current_date() and ")
				.append("sortNameTemporal.end > current_date()) ");
	}

}
