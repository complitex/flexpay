package org.flexpay.orgs.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Language;

public class BankDescription extends OrganizationInstanceDescription {

	public BankDescription() {
	}

	public BankDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
