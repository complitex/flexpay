package org.flexpay.bti.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.persistence.history.Diff;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public class BuildingAttributeHistoryBuilder extends HistoryBuilderBase<Building> {

	public static final int FIELD_ATTRIBUTE_ID = 1;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	protected void doDiff(@Nullable Building t1, @NotNull Building t2, @NotNull Diff diff) {
	}

	/**
	 * Apply diff to an object
	 *
	 * @param building	Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull Building building, @NotNull Diff diff) {
	}
}
