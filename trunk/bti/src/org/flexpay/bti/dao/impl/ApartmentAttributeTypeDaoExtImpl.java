package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.ApartmentAttributeTypeDao;
import org.flexpay.bti.dao.ApartmentAttributeTypeDaoExt;
import org.flexpay.bti.dao.ApartmentAttributeTypeEnumDao;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeTypeEnum;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeTypeEnumValue;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class ApartmentAttributeTypeDaoExtImpl extends JpaDaoSupport implements ApartmentAttributeTypeDaoExt {

	private ApartmentAttributeTypeDao attributeTypeDao;
	private ApartmentAttributeTypeEnumDao attributeTypeEnumDao;

	public ApartmentAttributeType readFull(Long id) {
		ApartmentAttributeType type = attributeTypeDao.readFull(id);

		// fetch necessary values for enum
		if (type instanceof ApartmentAttributeTypeEnum) {
			List<ApartmentAttributeTypeEnumValue> values = attributeTypeEnumDao.findValues(type.getId());
			ApartmentAttributeTypeEnum enumType = (ApartmentAttributeTypeEnum) type;
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
		Object[] params = {name, name, typeId, typeId == null || typeId.equals(0L) ? 1 : 0};
		List<?> result = getJpaTemplate().findByNamedQuery("ApartmentAttributeType.checkUniqueName", params);
		return result.isEmpty();
	}

	@Required
	public void setAttributeTypeDao(ApartmentAttributeTypeDao attributeTypeDao) {
		this.attributeTypeDao = attributeTypeDao;
	}

	@Required
	public void setAttributeTypeEnumDao(ApartmentAttributeTypeEnumDao attributeTypeEnumDao) {
		this.attributeTypeEnumDao = attributeTypeEnumDao;
	}

}