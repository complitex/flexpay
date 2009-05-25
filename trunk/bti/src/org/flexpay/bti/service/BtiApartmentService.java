package org.flexpay.bti.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BtiApartmentService {

	/**
	 * Read bti apartment with associated attributes
	 *
	 * @param stub apartment stub to read
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Nullable
	BtiApartment readWithAttributes(Stub<? extends Apartment> stub);

	/**
	 * Update apartment attributes
	 *
	 * @param apartment Apartment to update
	 * @return apartment back
	 */
	BtiApartment updateAttributes(@NotNull BtiApartment apartment);

}
