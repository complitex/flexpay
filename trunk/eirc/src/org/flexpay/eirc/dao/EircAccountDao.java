package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.EircAccount;

import java.util.Collection;
import java.util.List;

public interface EircAccountDao extends GenericDao<EircAccount, Long> {
	
	List<EircAccount> findObjects(Page<EircAccount> pager);
	
	List<EircAccount> findByApartment(Long id, Page<EircAccount> pager);

    List<EircAccount> findByApartments(Collection<Long> apartmentIds);

	List<EircAccount> findByApartmentAndFIO(Long id, String personFio, String consumerFio, Page<EircAccount> pager);

	List<EircAccount> findByBuildingAndFIO(Long id, String personFio, String consumerFio, Page<EircAccount> pager);

	/**
	 * Find accounts by person info
	 *
	 * @param searchString Person info search string
	 * @param ciSearchString Consumer info search string
	 * @param pager		Pager
	 * @return List of accounts
	 */
	List<EircAccount> findByPersonFIO(String searchString, String ciSearchString, Page<EircAccount> pager);

	List<EircAccount> findByNumber(String accountNumber);
}
