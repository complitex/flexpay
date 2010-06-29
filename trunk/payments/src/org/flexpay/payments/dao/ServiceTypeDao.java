package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.ServiceType;

import java.util.Collection;
import java.util.List;

public interface ServiceTypeDao extends GenericDao<ServiceType, Long> {

	/**
	 * List service types
	 *
	 * @param pager Page
	 * @return List of Service Types on current page
	 */
	List<ServiceType> findServiceTypes(Page<ServiceType> pager);

	/**
	 * List all service types
	 * @return list of service types
	 */
	List<ServiceType> findAllServiceTypes();

    List<ServiceType> findByCodes(Collection<Integer> codes);

}
