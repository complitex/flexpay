package org.flexpay.bti.service;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BtiApartmentService {

	/**
	 * Read bti apartment with associated attributes
	 *
	 * @param stub apartment stub to read
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Nullable
	BtiApartment readWithAttributes(Stub<BtiApartment> stub);

	/**
	 * Update apartment attributes
	 *
	 * @param apartment Apartment to update
	 * @return apartment back
	 */
	BtiApartment updateAttributes(@NotNull BtiApartment apartment);

	/**
	 * Find all BtiApartment in the town
	 * 
	 * @param town town to search
	 * @return BtiApartment list in town
	 */
	List<BtiApartment> findByTown(Stub<Town> town);

}
