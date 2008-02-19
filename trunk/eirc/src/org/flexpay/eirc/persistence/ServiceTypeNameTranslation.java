package org.flexpay.eirc.persistence;

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
}
