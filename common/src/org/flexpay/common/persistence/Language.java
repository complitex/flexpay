package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.propertyeditors.LocaleEditor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class Language implements Serializable {

	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_DISABLED = 1;

	private Long id;
	private boolean isDefault = false;
	private String langIsoCode;
	private int status;

	private Set<LangNameTranslation> translations = Collections.emptySet();

	/**
	 * Constructs a new Language.
	 */
	public Language() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(Long id) {
		this.id = id;
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
	public Collection<LangNameTranslation> getTranslations() {
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
	 * Getter for property 'status'.
	 *
	 * @return Value for property 'status'.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Setter for property 'status'.
	 *
	 * @param status Value to set for property 'status'.
	 */
	public void setStatus(int status) {
		this.status = status;
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
				.append("id", id)
				.append("Default", isDefault)
				.append("Status", status)
				.append("Iso Code", langIsoCode)
				.append("Translations", translations.toArray())
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
}
