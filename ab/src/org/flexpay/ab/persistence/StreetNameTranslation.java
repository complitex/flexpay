package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * StreetNameTranslation
 */
public class StreetNameTranslation extends Translation {

	public StreetNameTranslation() {
	}

	public StreetNameTranslation(@NotNull String name) throws Exception {
		super(name, getDefaultLanguage());
	}

	public StreetNameTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("Id", getId())
				.append("Name", getName())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return this == o || o instanceof StreetNameTranslation && super.equals(o);
	}
}
