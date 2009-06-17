package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class OrganizationName extends Translation {

	public OrganizationName() {
	}

	public OrganizationName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
