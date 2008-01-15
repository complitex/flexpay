package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;

/**
 * TownName
 */
public class TownName extends TemporaryName<TownName, TownNameTranslation> {

	public TownName() {
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return super.toString();
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
		} else if (!(obj instanceof TownName)) {
			return false;
		}

		return super.equals(obj);
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public TownName getEmpty() {
		return new TownName();
	}
}
