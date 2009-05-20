package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface ApartmentAttributeTypeDao extends GenericDao<ApartmentAttributeType, Long> {

	List<ApartmentAttributeType> findTypes(Page<ApartmentAttributeType> pager);

    List<ApartmentAttributeType> findAllTypes();

	List<ApartmentAttributeType> findTypesByName(String typeName, String code);

}
