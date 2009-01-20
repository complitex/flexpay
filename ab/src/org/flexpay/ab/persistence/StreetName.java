package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.exception.FlexPayException;

/**
 * StreetName
 */
public class StreetName extends TemporaryName<StreetName, StreetNameTranslation> {

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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof StreetName)) {
			return false;
		}

		return super.equals(obj);
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public StreetName getEmpty() {
		StreetName empty = new StreetName();
		empty.setObject(getObject());
		return empty;
	}
}
