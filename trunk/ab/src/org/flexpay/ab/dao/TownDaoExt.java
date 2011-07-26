package org.flexpay.ab.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface TownDaoExt extends JpaSetDao {

	/**
	 * Find and sort towns
	 *
	 * @param regionId Region key
	 * @param sorters  Collection of sorters
	 * @param pager	Pager
	 * @return List of towns
	 */
	@NotNull
	List<Town> findTowns(Long regionId, Collection<? extends ObjectSorter> sorters, Page<Town> pager);

    @NotNull
    Town findTown(@NotNull ArrayStack arrayStack);

}
