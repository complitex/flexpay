package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.EircAccount;

public interface EircAccountDao extends GenericDao<EircAccount, Long> {
	
	List<EircAccount> findObjects(Page<EircAccount> pager);
	
	List<EircAccount> findByApartment(Long id, Page<EircAccount> pager);


	/**
	 * Find accounts by person info
	 *
	 * @param searchString Person info search string
	 * @param pager		Pager
	 * @return List of accounts
	 */
	List<EircAccount> findByPersonFIO(String searchString, Page<EircAccount> pager);
}
