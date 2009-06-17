package org.flexpay.orgs.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Language;

public class PaymentsCollectorDescription extends OrganizationInstanceDescription {

	public PaymentsCollectorDescription() {
	}

	public PaymentsCollectorDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
