package org.flexpay.ab.persistence.sorter;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.sorter.I18nObjectSorter;
import org.jetbrains.annotations.NotNull;

public abstract class TownSorter extends I18nObjectSorter {

	protected String townField;

	public TownSorter() {
	}

	public TownSorter(@NotNull Language lang) {
		super(lang);
	}

	public void setTownField(String townField) {
		this.townField = townField;
	}
}
