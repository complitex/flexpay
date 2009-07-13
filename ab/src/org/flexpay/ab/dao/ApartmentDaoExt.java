package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Collection;

public interface ApartmentDaoExt {

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
	List<Apartment> findApartments(Long buildingId, Collection<ObjectSorter> sorters, Page<Apartment> pager);
}
