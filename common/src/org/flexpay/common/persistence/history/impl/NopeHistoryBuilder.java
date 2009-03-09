package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NopeHistoryBuilder <T extends DomainObject> implements HistoryBuilder<T> {

	/**
	 * Create diff from t1 to t2
	 *
	 * @param t1 First object
	 * @param t2 Second object
	 * @return Diff object
	 */
	@NotNull
	public Diff diff(@Nullable T t1, @NotNull T t2) {
		return new Diff();
	}

	/**
	 * Create diff for deleted object
	 *
	 * @param obj object to build diff for
	 * @return Diff object
	 */
	@NotNull
	public Diff deleteDiff(@NotNull T obj) {
		return new Diff();
	}

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	public void patch(@NotNull T t, @NotNull Diff diff) {
	}
}
