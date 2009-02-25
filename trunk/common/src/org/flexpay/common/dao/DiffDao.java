package org.flexpay.common.dao;

import org.flexpay.common.persistence.history.Diff;

import java.util.List;

public interface DiffDao extends GenericDao<Diff, Long> {

	/**
	 * Find diffs for object
	 *
	 * @param objectId Object key to get changes for
	 * @param objectType Object type
	 * @return list of change sets
	 */
	List<Diff> findDiffs(Long objectId, int objectType);
}
