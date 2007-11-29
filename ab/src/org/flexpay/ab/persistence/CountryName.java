package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "country_names_tbl",
		uniqueConstraints = {
		@UniqueConstraint (columnNames = {"language_id", "country_id"})
				})
public class CountryName implements Serializable {
	private Long id;
	private Language language;
	private Country country;
	private String name;
	private String shortName;

	private LangNameTranslation translation;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	@Basic
	public String getName() {
		return name;
	}

	/**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for property 'shortName'.
	 *
	 * @return Value for property 'shortName'.
	 */
	@Basic
	public String getShortName() {
		return shortName;
	}

	/**
	 * Setter for property 'shortName'.
	 *
	 * @param shortName Value to set for property 'shortName'.
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	 * Getter for property 'country'.
	 *
	 * @return Value for property 'country'.
	 */
	@ManyToOne
	@JoinColumn (name = "country_id")
	public Country getCountry() {
		return country;
	}

	/**
	 * Setter for property 'country'.
	 *
	 * @param country Value to set for property 'country'.
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Getter for property 'translation'.
	 *
	 * @return Value for property 'translation'.
	 */
	@Transient
	public LangNameTranslation getTranslation() {
		return translation;
	}

	/**
	 * Setter for property 'translation'.
	 *
	 * @param translation Value to set for property 'translation'.
	 */
	public void setTranslation(LangNameTranslation translation) {
		this.translation = translation;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("Language", language.getLangIsoCode())
				.append("Name", name)
				.append("Short name", shortName)
				.toString();
	}
}
