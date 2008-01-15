package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Translation;

/**
 * TownTypeTranslation is a trnaslation of TownType to particular language
 */
public class TownTypeTranslation extends Translation {

	private TownType townType;

	/**
	 * Constructs a new TownTypeTranslation.
	 */
	public TownTypeTranslation() {
	}

	/**
	 * Getter for property 'townType'.
	 *
	 * @return Value for property 'townType'.
	 */
	public TownType getTownType() {
		return townType;
	}

	/**
	 * Setter for property 'townType'.
	 *
	 * @param townType Value to set for property 'townType'.
	 */
	public void setTownType(TownType townType) {
		this.townType = townType;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("Id", getId())
				.append("Language", getLang().getLangIsoCode())
				.append("Name", getName())
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
		} else if (!(o instanceof TownTypeTranslation)) {
			return false;
		}
		return super.equals(o);
	}
}
