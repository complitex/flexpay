package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

/**
 * TownTypeTranslation is a trnaslation of TownType to particular language
 */
public class TownTypeTranslation extends Translation {
	
	private String shortName;

	/**
	 * Constructs a new TownTypeTranslation.
	 */
	public TownTypeTranslation() {
	}

	public TownTypeTranslation(@NotNull String name) {
		super(name, ApplicationConfig.getDefaultLanguage());
	}

	public TownTypeTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	/**
	 * Getter for property 'shortName'.
	 *
	 * @return Value for property 'shortName'.
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Setter for property 'shortName'.
	 *
	 * @param shortName Value to set for property 'shortName'.
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void copyName(Translation t) {
		super.copyName(t);
		if (t instanceof TownTypeTranslation) {
			this.shortName = ((TownTypeTranslation) t).getShortName();
		}
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
		} else if (!(o instanceof TownTypeTranslation)) {
			return false;
		}
		return super.equals(o);
	}
}
