package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * BuildingAttributeTypeTranslation
 */
public class BuildingAttributeTypeTranslation extends Translation {

	private String shortName;

	public BuildingAttributeTypeTranslation() {
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public BuildingAttributeTypeTranslation(String name, Language lang) {
		super(name, lang);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (! (o instanceof BuildingAttributeTypeTranslation)) {
			return false;
		}

		BuildingAttributeTypeTranslation that = (BuildingAttributeTypeTranslation) o;
		return new EqualsBuilder()
				.append(shortName, that.getShortName())
				.appendSuper(super.equals(o))
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(shortName)
				.appendSuper(super.hashCode())
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("short name", shortName)
				.toString();
	}
}
