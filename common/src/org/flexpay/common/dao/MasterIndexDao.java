package org.flexpay.common.dao;

import org.flexpay.common.persistence.MasterIndex;

import java.util.List;

public interface MasterIndexDao extends GenericDao<MasterIndex, Long> {

	List<MasterIndex> findIndexes(int type);
}
