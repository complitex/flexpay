package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.Collections;

/**
 * TownName
 */
public class TownName extends TemporaryName<TownName, TownNameTranslation> {

	public TownName() {
	}

	/**
	 * Copy constructor
	 *
	 * @param townName Town name to copy from
	 */
	@SuppressWarnings ({"unchecked"})
	public TownName(@Nullable TownName townName) {
		Set<TownNameTranslation> translations = townName != null ? townName.getTranslations() : Collections.EMPTY_SET;
		for (TownNameTranslation translation : translations) {
			TownNameTranslation copy = new TownNameTranslation(
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
		TownName empty = new TownName();
		empty.setObject(getObject());
		return empty;
	}
}
