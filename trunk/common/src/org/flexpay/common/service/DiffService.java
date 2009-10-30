package org.flexpay.common.service;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	 * Update existing diff
	 *
	 * @param diff History records set to update
	 * @return Diff object back
	 */
	@NotNull
	Diff update(@NotNull Diff diff);

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

	/**
	 * Fetch diffs got from last consumer update
	 *
	 * @param range Fetch range
	 * @return list of diffs, possibly empty
	 */
	@NotNull
	List<Diff> findNewDiffs(@NotNull FetchRange range);

	/**
	 * Check if there is some history for an object
	 *
	 * @param obj Object to check history existence for
	 * @return <code>true</code> if there is diffs, or <code>false</code> otherwise
	 */
	<T extends DomainObject> boolean hasDiffs(@NotNull T obj);

	/**
	 * Check if there is some history for all objects of a specified class
	 *
	 * @param clazz Objects class to check history existence for
	 * @return <code>true</code> if there is diffs for all objects of that class, or <code>false</code> otherwise
	 */
	<T extends DomainObject> boolean allObjectsHaveDiff(Class<T> clazz);
}
