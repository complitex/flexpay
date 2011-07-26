package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.history.Diff;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DiffDaoExt extends JpaSetDao {

	@NotNull
	List<Diff> findNewHistoryRecords(FetchRange range);

	boolean hasDiffs(Long objectId, int objectType);

	void removeDiffs(int processingStatus);

	void updateDiffsProcessingStatus(int statusOld, int statusNew);
}
