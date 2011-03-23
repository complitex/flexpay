package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public abstract class OrganizationInstanceDescription extends Translation {

	protected OrganizationInstanceDescription() {
	}

	protected OrganizationInstanceDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
