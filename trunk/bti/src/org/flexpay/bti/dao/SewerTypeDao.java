package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.SewerType;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface SewerTypeDao extends GenericDao<SewerType, Long> {

	List<SewerType> findSewerTypes(Page<SewerType> pager);

}
