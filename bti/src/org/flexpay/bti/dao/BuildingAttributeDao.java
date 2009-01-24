package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.BuildingAttribute;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface BuildingAttributeDao extends GenericDao<BuildingAttribute, Long> {

	List<BuildingAttribute> findAttributes(Long buildingId, Page<BuildingAttribute> pager);
}
