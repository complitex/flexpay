package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

public class SubdivisionName extends Translation {

	public SubdivisionName() {
	}

	public SubdivisionName(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

}
