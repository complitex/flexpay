package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class PaymentCollectorDescription extends OrganizationInstanceDescription {

	public PaymentCollectorDescription() {
	}

	public PaymentCollectorDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
