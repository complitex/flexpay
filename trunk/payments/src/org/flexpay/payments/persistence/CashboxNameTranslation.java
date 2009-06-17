package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

public class CashboxNameTranslation extends Translation {

	public CashboxNameTranslation() {
	}

	public CashboxNameTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}
}
