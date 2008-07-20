package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.TemporaryType;
import org.flexpay.common.persistence.Stub;

/**
 * StreetType entity class holds a general representation of various types of streets.
 */
public class StreetType extends TemporaryType<StreetType, StreetTypeTranslation> {

	/**
	 * Constructs a new StreetType.
	 */
	public StreetType() {
	}

	public StreetType(Long id) {
		super(id);
	}

	public StreetType(Stub<StreetType> stub) {
		super(stub.getId());
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
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
		} else if (!(obj instanceof StreetType)) {
			return false;
		}
		StreetType that = (StreetType) obj;

		return super.equals(that);
	}

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	public StreetType getEmpty() {
		return new StreetType();
	}
}
