package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Translation;

/**
 * StreetNameTranslation
 */
public class StreetNameTranslation extends Translation {

	public StreetNameTranslation() {
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
				//.append("Language", getLang().getLangIsoCode())
				.append("Name", getName())
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof StreetNameTranslation)) {
			return false;
		}
		return super.equals(o);
	}
}
