package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.BuildingAttributeTypeDao;
import org.flexpay.bti.dao.BuildingAttributeTypeDaoExt;
import org.flexpay.bti.dao.BuildingAttributeTypeEnumDao;
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.persistence.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.BuildingAttributeTypeEnumValue;
import org.flexpay.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

public class BuidingAttributeTypeDaoExtImpl implements BuildingAttributeTypeDaoExt {

	private BuildingAttributeTypeDao attributeTypeDao;
	private BuildingAttributeTypeEnumDao attributeTypeEnumDao;

	public BuildingAttributeType readFull(Long id) {
		BuildingAttributeType type = attributeTypeDao.readFull(id);

		// fetch necessary values for enum
		if (type instanceof BuildingAttributeTypeEnum) {
			List<BuildingAttributeTypeEnumValue> values = attributeTypeEnumDao.findValues(type.getId());
			Set<BuildingAttributeTypeEnumValue> valuesSet = CollectionUtils.set(values);
			BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;
			enumType.setValues(valuesSet);
		}

		return type;
	}

	@Required
	public void setAttributeTypeDao(BuildingAttributeTypeDao attributeTypeDao) {
		this.attributeTypeDao = attributeTypeDao;
	}

	@Required
	public void setAttributeTypeEnumDao(BuildingAttributeTypeEnumDao attributeTypeEnumDao) {
		this.attributeTypeEnumDao = attributeTypeEnumDao;
	}
}
