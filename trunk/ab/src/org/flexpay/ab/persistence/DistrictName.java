package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.Collections;

public class DistrictName extends TemporaryName<DistrictName, DistrictNameTranslation> {

	/**
	 * Constructs a new DistrictName.
	 */
	public DistrictName() {
	}

	@SuppressWarnings ({"unchecked", "CollectionsFieldAccessReplaceableByMethodCall"})
	public DistrictName(@Nullable DistrictName name) {
		Set<DistrictNameTranslation> translations = name != null ? name.getTranslations() : Collections.EMPTY_SET;
		for (DistrictNameTranslation translation : translations) {
			DistrictNameTranslation copy = new DistrictNameTranslation(
					translation.getName(), translation.getLang());
			addNameTranslation(copy);
		}
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
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public DistrictName getEmpty() {
		DistrictName empty = new DistrictName();
		empty.setObject(getObject());
		return empty;
	}
}
