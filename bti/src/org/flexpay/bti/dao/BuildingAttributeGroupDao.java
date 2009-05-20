package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface BuildingAttributeGroupDao extends GenericDao<BuildingAttributeGroup, Long> {

	List<BuildingAttributeGroup> findGroups(Page<BuildingAttributeGroup> pager);

	List<BuildingAttributeGroup> findAllGroups();

}
