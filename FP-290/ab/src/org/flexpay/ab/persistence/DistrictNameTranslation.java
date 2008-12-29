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
	 * @throws Exception if failure occurs
	 */
	public DistrictNameTranslation(@NotNull String name) throws Exception {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	/**
	 * Constructs a new DistrictNameTranslation.
	 *
	 * @param name Translation
	 * @param lang Language that translation is in
	 * @throws Exception if failure occurs
	 */
	public DistrictNameTranslation(@NotNull String name, @NotNull Language lang) throws Exception {
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
