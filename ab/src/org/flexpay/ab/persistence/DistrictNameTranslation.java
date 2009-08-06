package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class DistrictNameTranslation extends Translation {

	public DistrictNameTranslation() {
	}

	public DistrictNameTranslation(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public DistrictNameTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("name", getName())
				.append("language", getLang().getLangIsoCode())
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof DistrictNameTranslation && super.equals(o);
	}

}
