package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.ServiceType;

import java.util.List;

public interface ServiceTypeDao extends GenericDao<ServiceType, Long> {

	/**
	 * List service types
	 *
	 * @param pager Page
	 * @return List of Service Types on current page
	 */
	List<ServiceType> findServiceTypes(Page<ServiceType> pager);
}