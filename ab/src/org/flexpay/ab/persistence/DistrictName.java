package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;

public class DistrictName extends TemporaryName<DistrictName, DistrictNameTranslation> {

	/**
	 * Constructs a new DistrictName.
	 */
	public DistrictName() {
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
	public DistrictName getEmpty() {
		return new DistrictName();
	}
}
