package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.propertyeditors.LocaleEditor;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class Language extends DomainObjectWithStatus {

	private boolean isDefault = false;
	private String langIsoCode;

	private Set<LangNameTranslation> translations = Collections.emptySet();

	/**
	 * Constructs a new Language.
	 */
	public Language() {
	}

	public Language(Long id) {
		super(id);
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
	public Set<LangNameTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Set<LangNameTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * Get Language name according to ISO Language Code.
	 *
	 * @return Value for property 'name'.
	 */
	public String getLangIsoCode() {
		return langIsoCode;
	}

	/**
	 * Setter for property 'name'.
	 *
	 * @param langIsoCode Value to set for property 'name'.
	 */
	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("Default", isDefault)
				.append("Status", getStatus())
				.append("Iso Code", langIsoCode)
				.toString();
	}

	public Locale getLocale() {
		LocaleEditor editor = new LocaleEditor();
		editor.setAsText(langIsoCode);
		return (Locale) editor.getValue();
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Language)) {
			return false;
		}

		Language that = (Language) o;
		return new EqualsBuilder()
				.append(langIsoCode, that.getLangIsoCode())
				.isEquals();
	}

	public int hashCode() {
		return langIsoCode.hashCode();
	}

	public String getName() {
		for (LangNameTranslation translation : translations) {
			if (translation.getTranslationFrom().getId().equals(getId())) {
				return translation.getTranslation();
			}
		}

		return null;
	}
}
