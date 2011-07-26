package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface DistrictDaoExt extends JpaSetDao {

	/**
	 * Find and sort districts
	 *
	 * @param townId  Town key
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of districts
	 */
	@NotNull
	List<District> findDistricts(Long townId, Collection<? extends ObjectSorter> sorters, Page<District> pager);
}
