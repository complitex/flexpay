package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class DistrictNameTranslation extends Translation {

	/**
	 * Constructs a new DistrictNameTranslation.
	 */
	public DistrictNameTranslation() {
	}

	/**
	 * Constructs a new DistrictNameTranslation.
	 *
	 * @param name Translation
	 */
	public DistrictNameTranslation(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	/**
	 * Constructs a new DistrictNameTranslation.
	 *
	 * @param name Translation
	 * @param lang Language that translation is in
	 */
	public DistrictNameTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("Name", getName())
				.append("Language", getLang().getLangIsoCode())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {

		return o instanceof DistrictNameTranslation && super.equals(o);
	}
}
