package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Translation;

public class ServiceTypeNameTranslation extends Translation {

	private String description;

	/**
	 * Constructs a new Translation.
	 */
	public ServiceTypeNameTranslation() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("Name", getName())
				.append("Description", description)
				.append("Lang", getLang().getLangIsoCode())
				.toString();
	}
}
