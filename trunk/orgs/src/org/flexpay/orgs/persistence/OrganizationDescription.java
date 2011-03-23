package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class OrganizationDescription extends Translation {

	/**
	 * Constructs a new Translation.
	 */
	public OrganizationDescription() {
	}

	public OrganizationDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
