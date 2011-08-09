package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * TownName
 */
public class TownName extends TemporaryName<TownName, TownNameTranslation> {

	public TownName() {
	}

	@SuppressWarnings ({"unchecked", "CollectionsFieldAccessReplaceableByMethodCall"})
	public TownName(@Nullable TownName townName) {
		Set<TownNameTranslation> translations = townName != null ? townName.getTranslations() : new HashSet<TownNameTranslation>();;
		for (TownNameTranslation translation : translations) {
			TownNameTranslation copy = new TownNameTranslation(
					translation.getName(), translation.getLang());
			addNameTranslation(copy);
		}
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	@Override
	public TownName getEmpty() {
		TownName empty = new TownName();
		empty.setObject(getObject());
		return empty;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TownName)) {
			return false;
		}

		return super.equals(obj);
	}

}
