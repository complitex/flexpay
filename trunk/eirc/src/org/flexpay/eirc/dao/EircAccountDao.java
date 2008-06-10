package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.EircAccount;

public interface EircAccountDao extends GenericDao<EircAccount, Long> {
	
	List<EircAccount> findObjects(Page<EircAccount> pager);

}