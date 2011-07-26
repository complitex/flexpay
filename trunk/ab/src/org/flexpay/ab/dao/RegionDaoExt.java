package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Region;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface RegionDaoExt extends JpaSetDao {

	/**
	 * Find and sort regions
	 *
	 * @param countryId Country key
	 * @param sorters  Collection of sorters
	 * @param pager	Pager
	 * @return List of regions
	 */
	@NotNull
	List<Region> findRegions(Long countryId, Collection<? extends ObjectSorter> sorters, Page<Region> pager);

}
