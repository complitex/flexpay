package org.flexpay.common.persistence;

import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

public class MeasureUnitName extends Translation {

	/**
	 * Constructs a new Translation.
	 */
	public MeasureUnitName() {
	}

	/**
	 * Constructs a new Translation.
	 *
	 * @param name Unit name in default language
	 */
	public MeasureUnitName(@NotNull String name) {
		this(name, ApplicationConfig.getDefaultLanguage());
	}

	public MeasureUnitName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MeasureUnitName && super.equals(o);
	}

}
