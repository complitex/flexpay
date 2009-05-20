package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeGroup;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface ApartmentAttributeGroupDao extends GenericDao<ApartmentAttributeGroup, Long> {

	List<ApartmentAttributeGroup> findGroups(Page<ApartmentAttributeGroup> pager);

	List<ApartmentAttributeGroup> findAllGroups();

}
