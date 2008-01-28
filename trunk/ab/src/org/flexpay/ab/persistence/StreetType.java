package org.flexpay.ab.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collection;
import java.util.Collections;

/**
 * StreetType entity class holds a general representation of various types of streets.
 */
public class StreetType extends DomainObjectWithStatus {

	private Collection<StreetTypeTranslation> translations = Collections.emptyList();

	/**
	 * Constructs a new StreetType.
	 */
	public StreetType() {
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Collection<StreetTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'typeTranslations'.
	 */
	public void setTranslations(Collection<StreetTypeTranslation> translations) {
		this.translations = translations;
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
				.append("Translations", translations.toArray())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(translations.hashCode()).toHashCode();
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
				.append(translations, that.getTranslations())
				.isEquals();
	}
}
