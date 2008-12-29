package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Translation;

/**
 * IdentityTypeTranslation is a translation of IdentityType to particular language
 */
public class IdentityTypeTranslation extends Translation {

	/**
	 * Constructs a new IdentityTypeTranslation.
	 */
	public IdentityTypeTranslation() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof IdentityTypeTranslation && super.equals(o);
	}
}
