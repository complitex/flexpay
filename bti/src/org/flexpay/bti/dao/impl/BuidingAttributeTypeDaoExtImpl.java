package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.BuildingAttributeTypeDao;
import org.flexpay.bti.dao.BuildingAttributeTypeDaoExt;
import org.flexpay.bti.dao.BuildingAttributeTypeEnumDao;
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.persistence.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.BuildingAttributeTypeEnumValue;
import org.flexpay.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;
import java.util.Set;

public class BuidingAttributeTypeDaoExtImpl extends HibernateDaoSupport implements BuildingAttributeTypeDaoExt {

	private BuildingAttributeTypeDao attributeTypeDao;
	private BuildingAttributeTypeEnumDao attributeTypeEnumDao;

	public BuildingAttributeType readFull(Long id) {
		BuildingAttributeType type = attributeTypeDao.readFull(id);

		// fetch necessary values for enum
		if (type instanceof BuildingAttributeTypeEnum) {
			List<BuildingAttributeTypeEnumValue> values = attributeTypeEnumDao.findValues(type.getId());
			BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;
			enumType.getValues().addAll(values);
		}

		return type;
	}

	/**
	 * Check if there is only
	 *
	 * @param name   Translation to check
	 * @param typeId Type key
	 * @return <code>true</code> if this name is unique, or <code>false</code> otherwise
	 */
	public boolean isUniqueTypeName(String name, Long typeId) {
		Object[] params = {name, typeId, typeId == null || typeId.equals(0L) ? 1 : 0};
		List<?> result = getHibernateTemplate().findByNamedQuery("BuildingAttributeType.checkUniqueName", params);
		return result.isEmpty();
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
