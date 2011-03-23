package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class PaymentPointName extends Translation {

	public PaymentPointName() {
	}

	public PaymentPointName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
