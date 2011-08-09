package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TemporaryName;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * StreetName
 */
public class StreetName extends TemporaryName<StreetName, StreetNameTranslation> {

	public StreetName() {
	}

	@SuppressWarnings ({"unchecked", "CollectionsFieldAccessReplaceableByMethodCall"})
	public StreetName(@Nullable StreetName name) {
		Set<StreetNameTranslation> translations = name != null ? name.getTranslations() : new HashSet<StreetNameTranslation>();;
		for (StreetNameTranslation translation : translations) {
			StreetNameTranslation copy = new StreetNameTranslation(
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
	public StreetName getEmpty() {
		StreetName empty = new StreetName();
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
        } else if (!(obj instanceof StreetName)) {
            return false;
        }

        return super.equals(obj);
    }

}
