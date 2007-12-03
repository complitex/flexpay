package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.propertyeditors.LocaleEditor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Entity
@Table (name = "languages_tbl")
@NamedQueries ({
@NamedQuery (name = "Language.listLanguages", query = "FROM Language")
		})
public class Language implements Serializable {
	private Long id;
	private boolean isDefault = false;
	private String langIsoCode;
	private LanguageStatus status;
	private List<LangNameTranslation> translations = Collections.emptyList();

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
	@Id
	@GeneratedValue
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
	@Basic
	@Column (name = "is_default")
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
	@OneToMany (cascade = {}, mappedBy = "language", fetch = FetchType.EAGER)
	public Collection<LangNameTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(List<LangNameTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * Getter for property 'status'.
	 *
	 * @return Value for property 'status'.
	 */
	@Basic
	public LanguageStatus getStatus() {
		return status;
	}

	/**
	 * Setter for property 'status'.
	 *
	 * @param status Value to set for property 'status'.
	 */
	public void setStatus(LanguageStatus status) {
		this.status = status;
	}

	/**
	 * Get Language name according to ISO Language Code.
	 *
	 * @return Value for property 'name'.
	 */
	@Column (unique = true, nullable = false)
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
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("Default", isDefault)
				.append("Status", status)
				.append("Iso Code", langIsoCode)
				.append("Translations", translations.toArray())
				.toString();
	}

	@Transient
	public Locale getLocale() {
		LocaleEditor editor = new LocaleEditor();
		editor.setAsText(langIsoCode);
		return (Locale) editor.getValue();
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Language language = (Language) o;

		return langIsoCode.equals(language.getLangIsoCode());
	}

	public int hashCode() {
		return langIsoCode.hashCode();
	}
}
