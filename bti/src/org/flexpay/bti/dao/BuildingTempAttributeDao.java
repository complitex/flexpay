package org.flexpay.bti.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.bti.persistence.BuildingTempAttribute;

import java.util.List;

public interface BuildingTempAttributeDao extends GenericDao<BuildingTempAttribute, Long> {

	List<BuildingTempAttribute> findAttributes(Long buildingId, Page<BuildingTempAttribute> pager);
}
