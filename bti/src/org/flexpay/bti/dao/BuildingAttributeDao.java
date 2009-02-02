package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.BuildingAttributeBase;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface BuildingAttributeDao extends GenericDao<BuildingAttributeBase, Long> {

	List<BuildingAttributeBase> findAttributes(Long buildingId, Page<BuildingAttributeBase> pager);

}
