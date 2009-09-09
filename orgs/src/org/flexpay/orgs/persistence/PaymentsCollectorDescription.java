package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class PaymentsCollectorDescription extends OrganizationInstanceDescription {

	public PaymentsCollectorDescription() {
	}

	public PaymentsCollectorDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
