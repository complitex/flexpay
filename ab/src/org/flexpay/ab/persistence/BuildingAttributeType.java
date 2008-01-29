package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collections;
import java.util.Set;

/**
 * BuildingAttributeType
 */
public class BuildingAttributeType extends DomainObject {

	private Set<BuildingAttributeTypeTranslation> translations = Collections.emptySet();

	public BuildingAttributeType() {
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
	public int hashCode() {
		return new HashCodeBuilder()
				.append(translations.toArray()).toHashCode();
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
		return new EqualsBuilder()
				.append(translations, that.getTranslations())
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("translations", translations)
				.toString();
	}
}
