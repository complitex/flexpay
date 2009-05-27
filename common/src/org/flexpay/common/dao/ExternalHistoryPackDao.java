package org.flexpay.common.dao;

import org.flexpay.common.persistence.history.ExternalHistoryPack;

import java.util.List;

public interface ExternalHistoryPackDao extends GenericDao<ExternalHistoryPack, Long> {

	List<ExternalHistoryPack> findLatestPacks();
}
