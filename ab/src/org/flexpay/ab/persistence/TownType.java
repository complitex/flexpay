package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TemporaryType;
import org.jetbrains.annotations.NotNull;

/**
 * TownType entity class holds a general representation of various types of localities, such as towns, villages, etc.
 */
public class TownType extends TemporaryType<TownType, TownTypeTranslation> {

	public TownType() {
	}

	public TownType(Long id) {
		super(id);
	}

	public TownType(@NotNull Stub<TownType> stub) {
		super(stub.getId());
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	@Override
	public TownType getEmpty() {
		return new TownType();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TownType && super.equals(obj);
	}

}
