package org.flexpay.common.persistence.sorter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for HQL sorters that support i18n
 */
public abstract class I18nObjectSorter extends ObjectSorter {

	protected static final String TRANSLATION_1 = "sort_translation_1";
	protected static final String TRANSLATION_2 = "sort_translation_2";

	protected Language lang;

	protected I18nObjectSorter() {
		lang = ApplicationConfig.getDefaultLanguage();
	}

	protected I18nObjectSorter(@NotNull Language lang) {
		this.lang = lang;
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	/**
	 * Add HQL addendum for ORDER BY clause
	 *
	 * @param orderByClause HQL query to update
	 */
	@Override
	public void setOrderBy(StringBuilder orderByClause) {

		if (orderByClause.length() > 0) {
			orderByClause.append(",");
		}

		orderByClause.append(" upper(ifnull(")
				.append(TRANSLATION_1).append(".name, ")
				.append(TRANSLATION_2).append(".name)) ")
				.append(getOrder());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("order", getOrder()).
				append("active", isActivated()).
				append("language", getLang()).
				toString();
	}
}
