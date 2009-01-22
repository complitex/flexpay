package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

/**
 * BuildingAttributeTypeTranslation
 */
public class AddressAttributeTypeTranslation extends Translation {

	private String shortName;

	public AddressAttributeTypeTranslation() {
	}

	public AddressAttributeTypeTranslation(String name, Language lang) {
		super(name, lang);
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public boolean equals(Object o) {

		return o instanceof AddressAttributeTypeTranslation && super.equals(o);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("short name", shortName)
				.toString();
	}

	public boolean isBlank() {
		return StringUtils.isBlank(getName()) && StringUtils.isBlank(getShortName());
	}
}
