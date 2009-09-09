package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class BankDescription extends OrganizationInstanceDescription {

	public BankDescription() {
	}

	public BankDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
