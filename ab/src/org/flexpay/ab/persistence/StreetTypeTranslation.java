package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Translation;

/**
 * StreetTypeTranslation is a translation of StreetType to particular language
 */
public class StreetTypeTranslation extends Translation {

	/**
	 * Constructs a new StreetTypeTranslation.
	 */
	public StreetTypeTranslation() {
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
