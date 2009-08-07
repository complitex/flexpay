package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.Collections;

/**
 * RegionName
 */
public class RegionName extends TemporaryName<RegionName, RegionNameTranslation> {

	public RegionName() {
	}

	/**
	 * Copy constructor
	 *
	 * @param regionName Region name to copy from
	 */
	@SuppressWarnings ({"unchecked", "CollectionsFieldAccessReplaceableByMethodCall"})
	public RegionName(@Nullable RegionName regionName) {
		Set<RegionNameTranslation> translations = regionName != null ? regionName.getTranslations() : Collections.EMPTY_SET;
		for (RegionNameTranslation translation : translations) {
			RegionNameTranslation copy = new RegionNameTranslation(
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
	public RegionName getEmpty() {
		RegionName regionName = new RegionName();
		regionName.setObject(getObject());
		return regionName;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof RegionName)) {
			return false;
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
