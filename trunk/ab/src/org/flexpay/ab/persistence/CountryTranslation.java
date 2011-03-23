package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * CountryTranslation is a translation of Country to particular language
 */
public class CountryTranslation extends Translation {

	private String shortName;

	public CountryTranslation() {
	}

	public CountryTranslation(@NotNull String name) {
		super(name, getDefaultLanguage());
	}

	public CountryTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CountryTranslation)) {
			return false;
		}

		CountryTranslation that = (CountryTranslation) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(that))
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("language", getLang().getLangIsoCode())
				.append("name", getName())
				.append("shortName", shortName)
				.toString();
	}

}
