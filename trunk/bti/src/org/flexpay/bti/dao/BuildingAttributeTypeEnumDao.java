package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.building.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeEnumValue;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface BuildingAttributeTypeEnumDao extends GenericDao<BuildingAttributeTypeEnum, Long> {

	List<BuildingAttributeTypeEnumValue> findValues(Long enumId);

}
