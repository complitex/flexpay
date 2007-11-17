package org.flexpay.common.util.locale;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collection;

public class Language {
	private boolean isDefault = false;
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
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Default", isDefault)
				.append("Translations", translations.toArray())
				.toString();
	}
}
