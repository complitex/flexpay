package org.flexpay.ab.service.history;

import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.ab.persistence.TownType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

public class TownTypeHistoryBuilder implements HistoryBuilder<TownType> {

	public static final int FIELD_NAME = 1;
	public static final int FIELD_SHORT_NAME = 1;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Create diff from t1 to t2
	 *
	 * @param t1 First object
	 * @param t2 Second object
	 * @return Diff object
	 */
	@NotNull
	public Diff diff(@NotNull TownType t1, @NotNull TownType t2) {
		return null;
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull TownType t, @NotNull Diff diff) {
	}
}
