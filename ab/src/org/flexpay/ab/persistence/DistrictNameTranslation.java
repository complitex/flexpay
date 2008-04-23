package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Translation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DistrictNameTranslation extends Translation {

	/**
	 * Constructs a new DistrictNameTranslation.
	 */
	public DistrictNameTranslation() {
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
		if (this == o) {
			return true;
		} else if (!(o instanceof DistrictNameTranslation)) {
			return false;
		}

		return super.equals(o);
	}
}
