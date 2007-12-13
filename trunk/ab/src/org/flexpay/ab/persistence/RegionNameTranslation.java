package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Translation;

/**
 * RegionNameLangValue generated by hbm2java
 */
public class RegionNameTranslation extends Translation implements java.io.Serializable {

	private Long id;
	private RegionName regionName;

	private transient LangNameTranslation langTranslation;

	/**
	 * Constructs a new RegionNameTranslation.
	 */
	public RegionNameTranslation() {
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
	 * Getter for property 'regionName'.
	 *
	 * @return Value for property 'regionName'.
	 */
	public RegionName getRegionName() {
		return regionName;
	}

	/**
	 * Setter for property 'regionName'.
	 *
	 * @param regionName Value to set for property 'regionName'.
	 */
	public void setRegionName(RegionName regionName) {
		this.regionName = regionName;
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
				.append("Name", getName())
				.append("Language", getLang().getLangIsoCode())
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof RegionNameTranslation)) {
			return false;
		}

		return super.equals(o);
	}
}
