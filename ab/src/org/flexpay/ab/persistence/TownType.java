package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.TemporaryType;

/**
 * TownType entity class holds a general representation of various types of localities,
 * such as towns, villages, etc.
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


	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("Id", getId())
				.append("Status", getStatus())
				.append("Translations", getTranslations().toArray())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TownType)) {
			return false;
		}

		TownType that = (TownType) obj;

		return super.equals(that);
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
