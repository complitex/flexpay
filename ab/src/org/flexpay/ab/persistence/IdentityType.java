package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collection;
import java.util.Collections;

/**
 * IdentityType entity class holds a general representation of various types of
 * identities.
 */
public class IdentityType extends DomainObjectWithStatus {

	private Collection<IdentityTypeTranslation> translations = Collections.emptyList();

	/**
	 * Constructs a new IdentityType.
	 */
	public IdentityType() {
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Collection<IdentityTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Collection<IdentityTypeTranslation> translations) {
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
}
