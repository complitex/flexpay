package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
