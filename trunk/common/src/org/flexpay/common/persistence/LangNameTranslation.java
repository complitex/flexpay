package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Language name translation to particular locale
 */
@Entity
@Table (name = "language_names_tbl",
		uniqueConstraints = {
		@UniqueConstraint (columnNames = {"language_id", "translation_from_language_id"})
				})
public class LangNameTranslation implements Serializable {
	private Long id;
	private Language language;
	private String translation;
	private Language translationFrom;

	/**
	 * Constructs a new LanguageName.
	 */
	public LangNameTranslation() {
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
	 * Getter for property 'language'.
	 *
	 * @return Value for property 'language'.
	 */
	@ManyToOne
	@JoinColumn (name = "language_id")
	public Language getLanguage() {
		return language;
	}

	/**
	 * Setter for property 'language'.
	 *
	 * @param language Value to set for property 'language'.
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/**
	 * Getter for property 'translation'.
	 *
	 * @return Value for property 'translation'.
	 */
	public String getTranslation() {
		return translation;
	}

	/**
	 * Setter for property 'translation'.
	 *
	 * @param translation Value to set for property 'translation'.
	 */
	public void setTranslation(String translation) {
		this.translation = translation;
	}

	/**
	 * Getter for property 'translationFrom'.
	 *
	 * @return Value for property 'translationFrom'.
	 */
	@ManyToOne
	@JoinColumn (name = "translation_from_language_id")
	public Language getTranslationFrom() {
		return translationFrom;
	}

	/**
	 * Setter for property 'translationFrom'.
	 *
	 * @param translationFrom Value to set for property 'translationFrom'.
	 */
	public void setTranslationFrom(Language translationFrom) {
		this.translationFrom = translationFrom;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("From", translationFrom.getLangIsoCode())
				.append("Translation", translation)
				.toString();
	}

}
