package org.flexpay.ab.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface StreetDaoExt extends JpaSetDao {

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

	void deleteStreetDistricts(Street street);

	void deleteStreet(Street street);

    @Nullable
    Street findStreet(@NotNull ArrayStack filters);
}
