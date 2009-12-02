package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface StreetDaoExt {

	/**
	 * Find and sort streets
	 *
	 * @param townId  Town key
	 * @param sorters Collection of sorters
	 * @param query query
	 * @param languageId language id for search
	 * @param pager   Pager
	 * @return List of streets
	 */
	@NotNull
	List<Street> findByParentAndQuery(Long townId, Collection<? extends ObjectSorter> sorters, String query, Long languageId, Page<Street> pager);

	void deleteStreet(Street street);

}
