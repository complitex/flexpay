package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeTypeEnum;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeTypeEnumValue;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface ApartmentAttributeTypeEnumDao extends GenericDao<ApartmentAttributeTypeEnum, Long> {

	List<ApartmentAttributeTypeEnumValue> findValues(Long enumId);

}
