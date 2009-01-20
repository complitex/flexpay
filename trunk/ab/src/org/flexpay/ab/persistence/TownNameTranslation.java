package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.flexpay.ab.util.config.ApplicationConfig;

/**
 * Town name translation
 */
public class TownNameTranslation extends Translation {

	public TownNameTranslation() {
	}

	public TownNameTranslation(String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public TownNameTranslation(String name, Language language) {
		super(name, language);
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
				.append("Language", getLang().getLangIsoCode())
				.append("Name", getName())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof TownNameTranslation)) {
			return false;
		}
		return super.equals(o);
	}
}
