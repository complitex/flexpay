package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Translation;

/**
 * StreetTypeTranslation is a translation of StreetType to particular language
 */
public class StreetTypeTranslation extends Translation {

	private String shortName;

	/**
	 * Constructs a new StreetTypeTranslation.
	 */
	public StreetTypeTranslation() {
	}

	/**
	 * Getter for property 'shortName'.
	 * 
	 * @return Value for property 'shortName'.
	 */
	public String getShortName() {
		return shortName != null ? shortName : "";
	}

	/**
	 * Setter for property 'shortName'.
	 * 
	 * @param shortName
	 *            Value to set for property 'shortName'.
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof StreetTypeTranslation && super.equals(o);
	}
}
