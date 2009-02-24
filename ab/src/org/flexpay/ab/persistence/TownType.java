package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TemporaryType;
import org.jetbrains.annotations.NotNull;

/**
 * TownType entity class holds a general representation of various types of localities, such as towns, villages, etc.
 */
public class TownType extends TemporaryType<TownType, TownTypeTranslation> {

	/**
	 * Constructs a new TownType.
	 */
	public TownType() {
	}

	public TownType(Long id) {
		super(id);
	}

	public TownType(@NotNull Stub<TownType> stub) {
		super(stub.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {

		return obj instanceof TownType && super.equals(obj);
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public TownType getEmpty() {
		return new TownType();
	}
}
