package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface BuildingAttributeTypeDao extends GenericDao<BuildingAttributeType, Long> {

	List<BuildingAttributeType> findTypes(Page<BuildingAttributeType> pager);

    List<BuildingAttributeType> findAllTypes();

	List<BuildingAttributeType> findTypesByName(String typeName, String code);

}
