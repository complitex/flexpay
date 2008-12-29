package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

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
}
