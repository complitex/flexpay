package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

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

	public AddressAttributeTypeTranslation(@NotNull String name, String shortName, @NotNull Language lang) {
		super(name, lang);
		this.shortName = shortName;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isBlank() {
		return StringUtils.isBlank(getName()) && StringUtils.isBlank(getShortName());
	}

	@Override
	public void copyName(Translation t) {
		super.copyName(t);
		if (t instanceof AddressAttributeTypeTranslation) {
			this.shortName = ((AddressAttributeTypeTranslation) t).getShortName();
		}
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof AddressAttributeTypeTranslation && super.equals(o);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.appendSuper(super.toString())
				.append("shortName", shortName)
				.toString();
	}

}
