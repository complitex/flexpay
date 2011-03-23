package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * TownTypeTranslation is a translation of TownType to particular language
 */
public class TownTypeTranslation extends Translation {

	private String shortName;

	public TownTypeTranslation() {
	}

	public TownTypeTranslation(@NotNull String name) {
		super(name, getDefaultLanguage());
	}

	public TownTypeTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public void copyName(Translation t) {
		super.copyName(t);
		if (t instanceof TownTypeTranslation) {
			this.shortName = ((TownTypeTranslation) t).getShortName();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("language", getLang().getLangIsoCode()).
				append("name", getName()).
				append("shortName", shortName).
				toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof TownTypeTranslation)) {
			return false;
		}
		return super.equals(o);
	}

}
