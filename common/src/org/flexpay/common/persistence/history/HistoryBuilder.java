package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HistoryBuilder<T extends DomainObject> extends JpaSetService {

	/**
	 * Create diff from t1 to t2
	 *
	 * @param t1 First object
	 * @param t2 Second object
	 * @return Diff object
	 */
	@NotNull
	Diff diff(@Nullable T t1, @NotNull T t2);

	/**
	 * Create diff for deleted object
	 *
	 * @param obj object to build diff for
	 * @return Diff object
	 */
	@NotNull
	Diff deleteDiff(@NotNull T obj);

	/**
	 * Apply diff to an object
	 *
	 * @param t	Object to apply diff to
	 * @param diff Diff to apply
	 */
	void patch(@NotNull T t, @NotNull Diff diff);
}
