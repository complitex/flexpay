package org.flexpay.ab.persistence.sorter;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.sorter.I18nObjectSorter;
import org.jetbrains.annotations.NotNull;

public abstract class StreetSorter extends I18nObjectSorter {

	protected String streetField;

	public StreetSorter() {
	}

	public StreetSorter(@NotNull Language lang) {
		super(lang);
	}

	public void setStreetField(String streetField) {
		this.streetField = streetField;
	}
}