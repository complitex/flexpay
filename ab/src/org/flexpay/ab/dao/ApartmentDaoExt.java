package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.*;
import org.jetbrains.annotations.Nullable;

public interface ApartmentDaoExt {

	/**
	 * Find apartment by number
	 *
	 * @param building Building to find apartment in
	 * @param number   Building number
	 * @return Apartment instance, or <code>null</null> if not found
	 */
	@Nullable
	Apartment findApartmentStub(Building building, String number);
}
