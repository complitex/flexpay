package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.SewerMaterialType;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface SewerMaterialTypeDao extends GenericDao<SewerMaterialType, Long> {

    List<SewerMaterialType> findSewerMaterialTypes(Page<SewerMaterialType> pager);


}
