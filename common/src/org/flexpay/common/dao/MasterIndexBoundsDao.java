package org.flexpay.common.dao;

import org.flexpay.common.persistence.MasterIndexBounds;

import java.util.List;

public interface MasterIndexBoundsDao extends GenericDao<MasterIndexBounds, Long> {

	List<MasterIndexBounds> findIndexBoundses(int type);
}