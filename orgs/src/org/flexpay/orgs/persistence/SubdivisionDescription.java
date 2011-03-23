package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class SubdivisionDescription extends Translation {

	public SubdivisionDescription() {
	}

	public SubdivisionDescription(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
