package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * StreetTypeTranslation is a translation of StreetType to particular language
 */
public class StreetTypeTranslation extends Translation {

	private String shortName;

	/**
	 * Constructs a new StreetTypeTranslation.
	 */
	public StreetTypeTranslation() {
	}

	public StreetTypeTranslation(String name) throws Exception {
		super(name, getDefaultLanguage());
	}

	public StreetTypeTranslation(String name, Language language) {
		super(name, language);
	}

	public StreetTypeTranslation(String name, String shortName) throws Exception {
		super(name, getDefaultLanguage());
		this.shortName = shortName;
	}

	public StreetTypeTranslation(String name, String shortName, Language language) throws Exception {
		super(name, language);
		this.shortName = shortName;
	}

	/**
	 * Getter for property 'shortName'.
	 *
	 * @return Value for property 'shortName'.
	 */
	@NotNull
	public String getShortName() {
		return shortName != null ? shortName : "";
	}

	/**
	 * Setter for property 'shortName'.
	 *
	 * @param shortName Value to set for property 'shortName'.
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

    @Override
	public void copyName(Translation t) {
		super.copyName(t);
		if (t instanceof StreetTypeTranslation) {
			this.shortName = ((StreetTypeTranslation) t).getShortName();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("name", getName())
				.append("shortName", getShortName())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof StreetTypeTranslation && super.equals(o);
	}
}
