package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

public class RegionNameTranslation extends Translation {

	public RegionNameTranslation() {
	}

	public RegionNameTranslation(String name) {
		super(name, getDefaultLanguage());
	}

	public RegionNameTranslation(String name, Language language) {
		super(name, language);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("Name", getName())
				.append("Language", getLang().getLangIsoCode())
				.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof RegionNameTranslation)) {
			return false;
		}

		return super.equals(o);
	}

}
