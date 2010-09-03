package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * StreetName
 */
public class StreetName extends TemporaryName<StreetName, StreetNameTranslation> {

	public StreetName() {
	}

	@SuppressWarnings ({"unchecked", "CollectionsFieldAccessReplaceableByMethodCall"})
	public StreetName(@Nullable StreetName name) {
		Set<StreetNameTranslation> translations = name != null ? name.getTranslations() : Collections.EMPTY_SET;
		for (StreetNameTranslation translation : translations) {
			StreetNameTranslation copy = new StreetNameTranslation(
					translation.getName(), translation.getLang());
			addNameTranslation(copy);
		}

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
    @Override
	public StreetName getEmpty() {
		StreetName empty = new StreetName();
		empty.setObject(getObject());
		return empty;
	}

}
