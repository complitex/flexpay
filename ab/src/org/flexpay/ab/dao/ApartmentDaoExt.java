package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ApartmentDaoExt extends JpaSetDao {

	/**
	 * Find apartment by number
	 *
	 * @param building Building to find apartment in
	 * @param number   Building number
	 * @return Apartment instance, or <code>null</null> if not found
	 */
	@Nullable
	Stub<Apartment> findApartmentStub(@NotNull Building building, String number);

	/**
	 * Find and sort apartments
	 *
	 * @param buildingId  Building key
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of apartments
	 */
	@NotNull
	List<Apartment> findApartments(Long buildingId, Collection<? extends ObjectSorter> sorters, Page<Apartment> pager);
}
