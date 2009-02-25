package org.flexpay.common.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.DomainObject;

import java.util.List;

public interface DiffService {

	/**
	 * Persist a new Diff object
	 *
	 * @param diff Diff object to persist
	 * @return Diff back
	 */
	@NotNull
	Diff create(@NotNull Diff diff);

	/**
	 * Read diff with all records fetched
	 *
	 * @param stub Diff stub
	 * @return Diff if found, or <code>null</code> otherwise
	 */
	@Nullable
	Diff readFull(@NotNull Stub<Diff> stub);

	/**
	 * Find existing diffs for domain object
	 *
	 * @param obj Domain object to find history diffs for
	 * @param <T> Object type
	 * @return List of diffs for object
	 */
	@NotNull
	<T extends DomainObject> List<Diff> findDiffs(@NotNull T obj);
}
