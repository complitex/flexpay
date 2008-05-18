package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

import java.util.Collections;
import java.util.Set;

/**
 * BuildingAttributeType
 */
public class BuildingAttributeType extends DomainObject {

	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_NUMBER = 1;
	public static final int TYPE_BULK = 2;

	private int type = TYPE_UNKNOWN;
	private Set<BuildingAttributeTypeTranslation> translations = Collections.emptySet();

	public BuildingAttributeType() {
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Set<BuildingAttributeTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
	public void setTranslations(Set<BuildingAttributeTypeTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof BuildingAttributeType)) {
			return false;
		}

		BuildingAttributeType that = (BuildingAttributeType) obj;
		return type == that.type;
	}

	public int hashCode() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("type", type)
				.toString();
	}

	/**
	 * Check if attribute type is a building number
	 *
	 * @return <code>true</code> if attribute type is a building number
	 */
	public boolean isBuildingNumber() {
		return type == TYPE_NUMBER;
	}

	/**
	 * Check if attribute type is a bulk number
	 *
	 * @return <code>true</code> if attribute type is a bulk number
	 */
	public boolean isBulkNumber() {
		return type == TYPE_BULK;
	}
}
