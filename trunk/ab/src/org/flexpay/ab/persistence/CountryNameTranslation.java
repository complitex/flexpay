package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Translation;

import java.io.Serializable;

public class CountryNameTranslation extends Translation implements Serializable {
	private Long id;
	private Country country;
	private String shortName;

	private transient LangNameTranslation langTranslation;

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
	public LangNameTranslation getLangTranslation() {
		return langTranslation;
	}

	/**
	 * Setter for property 'translation'.
	 *
	 * @param langTranslation Value to set for property 'translation'.
	 */
	public void setLangTranslation(LangNameTranslation langTranslation) {
		this.langTranslation = langTranslation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", id)
				.append("Language", getLang().getLangIsoCode())
				.append("Name", getName())
				.append("Short name", shortName)
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CountryNameTranslation)) {
			return false;
		}

		CountryNameTranslation that = (CountryNameTranslation) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(that))
				.isEquals();
	}
}
