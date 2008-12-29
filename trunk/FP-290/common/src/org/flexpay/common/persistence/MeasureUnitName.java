package org.flexpay.common.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.util.config.ApplicationConfig;

public class MeasureUnitName extends Translation {

	/**
	 * Constructs a new Translation.
	 */
	public MeasureUnitName() {
	}

	/**
	 * Constructs a new Translation.
	 */
	public MeasureUnitName(@NotNull String name) throws Exception {
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
