package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.TemporaryType;

/**
 * StreetType entity class holds a general representation of various types of streets.
 */
public class StreetType extends TemporaryType<StreetType, StreetTypeTranslation> {

	/**
	 * Constructs a new StreetType.
	 */
	public StreetType() {
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Id", getId())
				.append("Status", getStatus())
				.append("Translations", getTranslations().toArray())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(getTranslations().hashCode()).toHashCode();
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

		return new EqualsBuilder()
				.appendSuper(super.equals(that))
				.append(getTranslations(), that.getTranslations())
				.isEquals();
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
