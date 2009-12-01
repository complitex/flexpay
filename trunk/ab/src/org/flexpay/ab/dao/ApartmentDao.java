package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface ApartmentDao extends GenericDao<Apartment, Long> {

	/**
	 * Find apartments in the building
	 *
	 * @param buildingsId Building address identifier
	 * @param pager	   Page instance
	 * @return list of apartments in the building
	 */
	List<Apartment> findObjects(Long buildingsId, Page<Apartment> pager);

	/**
	 * Find apartments in the building
	 *
	 * @param buildingsId Building address identifier
	 * @return list of apartments in the building
	 */
	List<Apartment> findObjects(Long buildingsId);

	/**
	 * Read apartment with registered persons
	 *
	 * @param apartmentId Apartment identifier
	 * @return List of apartments
	 */
	List<Apartment> findWithPersonsFull(Long apartmentId);

	List<Apartment> findWithFullHierarchy(Long apartmentId);

	List<Apartment> findSimpleByTown(Long townId, FetchRange range);

}
