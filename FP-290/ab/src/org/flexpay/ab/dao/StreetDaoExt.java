package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface StreetDaoExt {

	void invalidateTypeTemporals(Long streetId, Date futureInfinity, Date invalidDate);

	/**
	 * Check if street is in a town
	 *
	 * @param townId   Town key to check in
	 * @param streetId Street key to check
	 * @return <code>true</code> if requested street is in a town
	 */
	boolean isStreetInTown(Long townId, Long streetId);

	/**
	 * Find and sort streets
	 *
	 * @param townId  Town key
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of streets
	 */
	@NotNull
	List<Street> findStreets(Long townId, Collection<ObjectSorter> sorters, Page<Street> pager);
}