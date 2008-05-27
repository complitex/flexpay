package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface ApartmentDao extends GenericDao<Apartment, Long> {

	/**
	 * Find apartments in the building
	 *
	 * @param buildingId Building identifier
	 * @param pager	  Page instance
	 * @return list of apartments in the building
	 */
	List<Apartment> findObjects(Long buildingId, Page pager);
	
	/**
	 * Read apartment with registered persons
	 * 
	 * @param id Object identifier
	 * @return Object if found, or <code>null</code> otherwise
	 */
	List<Apartment> findWithPersonsFull(Long id);
}
