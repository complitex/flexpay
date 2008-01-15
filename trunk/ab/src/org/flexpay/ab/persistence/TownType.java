package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.TemporaryValueWithStatus;

import java.util.Collection;
import java.util.Collections;

/**
 * TownType entity class holds a general representation of various types of localities,
 * such as towns, villages, etc.
 */
public class TownType extends TemporaryValueWithStatus<TownType> {

	private Collection<TownTypeTranslation> translations = Collections.emptyList();

	/**
	 * Constructs a new TownType.
	 */
	public TownType() {
	}

	/**
	 * Getter for property 'typeTranslations'.
	 *
	 * @return Value for property 'typeTranslations'.
	 */
	public Collection<TownTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'typeTranslations'.
	 *
	 * @param translations Value to set for property 'typeTranslations'.
	 */
	public void setTranslations(Collection<TownTypeTranslation> translations) {
		this.translations = translations;
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
				.append("Translations", translations.toArray())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(translations)
				.toHashCode();
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

		return new EqualsBuilder()
				.append(translations, that.getTranslations())
				.isEquals();
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
