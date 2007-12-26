package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.TemporaryValue;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * RegionName
 */
public class RegionName implements TemporaryValue<RegionName>, Serializable {

	private Long id;
	private Region region;
	private Set<RegionNameTranslation> translations = Collections.emptySet();

	private transient RegionNameTranslation translation;

	/**
	 * Constructs a new RegionName.
	 */
	public RegionName() {
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return this.id;
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
	 * Getter for property 'region'.
	 *
	 * @return Value for property 'region'.
	 */
	public Region getRegion() {
		return this.region;
	}

	/**
	 * Setter for property 'region'.
	 *
	 * @param region Value to set for property 'region'.
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Set<RegionNameTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Set<RegionNameTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * Get transient property 'translation'.
	 *
	 * @return Value for property 'translation'.
	 */
	public RegionNameTranslation getTranslation() {
		return translation;
	}

	/**
	 * Setter for property 'translation'.
	 *
	 * @param translation Value to set for property 'translation'.
	 */
	public void setTranslation(RegionNameTranslation translation) {
		this.translation = translation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", id)
				.append("Translations", translations.toArray())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof RegionName)) {
			return false;
		}

		RegionName that = (RegionName) obj;
		return new EqualsBuilder()
				.append(translations, that.getTranslations()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(translations).toHashCode();
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public RegionName getEmpty() {
		return new RegionName();
	}
}
