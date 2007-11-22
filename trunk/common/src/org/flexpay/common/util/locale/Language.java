package org.flexpay.common.util.locale;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class Language {
	private boolean isDefault = false;
	private Locale locale;
	private Collection<LanguageName> translations = new ArrayList<LanguageName>(3);

	/**
	 * Constructs a new Language.
	 */
	public Language() {
	}

	/**
	 * Getter for property 'default'.
	 *
	 * @return Value for property 'default'.
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * Setter for property 'default'.
	 *
	 * @param aDefault Value to set for property 'default'.
	 */
	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Collection<LanguageName> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Collection<LanguageName> translations) {
		this.translations = translations;
	}

	public void addTranslation(LanguageName languageName) {
		translations.add(languageName);
	}

	/**
	 * Getter for property 'locale'.
	 *
	 * @return Value for property 'locale'.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Setter for property 'locale'.
	 *
	 * @param locale Value to set for property 'locale'.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Setter for property 'locale'.
	 *
	 * @param locale Value to set for property 'locale'.
	 */
	public void setLocaleName(String locale) {
		this.locale = new Locale(locale);
	}

	/**
	 * Getter for property 'localeName'.
	 *
	 * @return Value for property 'localeName'.
	 */
	public String getLocaleName() {
		return locale.getCountry();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Default", isDefault)
				.append("Locale", locale.getDisplayName())
				.append("Translations", translations.toArray())
				.toString();
	}
}
