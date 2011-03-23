package org.flexpay.ab.persistence.sorter;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * Sorter that sorts towns by type
 */
public class TownSorterByType extends TownSorter {

	public TownSorterByType() {
	}

	public TownSorterByType(@NotNull Language lang) {
		super(lang);
	}

    @Override
	public void setFrom(StringBuilder query) {
		Language defaultLang = getDefaultLanguage();
		query
				.append(" left join ").append(townField).append(".typeTemporals sortTypeTemporal ")
				.append(" left join sortTypeTemporal.value sortTypeTemporalValue ")
				.append(" left outer join sortTypeTemporalValue.translations ")
				.append(TRANSLATION_1).append(" with (" + TRANSLATION_1 + ".lang.id=").append(lang.getId()).append(")")
				.append(" left outer join sortTypeTemporalValue.translations ")
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
				.append("sortTypeTemporal.invalidDate='2100-12-31' and ")
				.append("sortTypeTemporal.begin <= current_date() and ")
				.append("sortTypeTemporal.end > current_date()) ");
	}
}
