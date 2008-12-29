package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;

/**
 * RegionName
 */
public class RegionName extends TemporaryName<RegionName, RegionNameTranslation> {

	/**
	 * Constructs a new RegionName.
	 */
	public RegionName() {
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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof RegionName)) {
			return false;
		}

		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
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
