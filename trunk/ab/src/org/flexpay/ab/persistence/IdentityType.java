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

	public static int TYPE_UNKNOWN = 0;
	public static int TYPE_PASSPORT = 1;
	public static int TYPE_FOREIGN_PASSPORT = 2;

	private int typeId = TYPE_UNKNOWN;

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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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
