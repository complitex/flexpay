package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;

import java.io.Serializable;

public class CountryNameTranslation implements Serializable {
	private Long id;
	private Language lang;
	private Country country;
	private String name;
	private String shortName;

	private transient LangNameTranslation translation;

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
	public Language getLang() {
		return lang;
	}

	/**
	 * Setter for property 'language'.
	 *
	 * @param lang Value to set for property 'language'.
	 */
	public void setLang(Language lang) {
		this.lang = lang;
	}

	/**
	 * Getter for property 'country'.
	 *
	 * @return Value for property 'country'.
	 */
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
				.append("Language", lang.getLangIsoCode())
				.append("Name", name)
				.append("Short name", shortName)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(lang)
				.append(name)
				.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CountryNameTranslation)) {
			return false;
		}
		CountryNameTranslation translation = (CountryNameTranslation) obj;
		return new EqualsBuilder()
				.append(lang, translation.getLang())
				.append(name, translation.getName())
				.isEquals();
	}
}
