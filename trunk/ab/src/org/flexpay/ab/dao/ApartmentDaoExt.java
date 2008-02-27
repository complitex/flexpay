package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.*;

public interface ApartmentDaoExt {

	/**
	 * Find apartment by number
	 *
	 * @param building Building to find apartment in
	 * @param number   Building number
	 * @return Apartment instance, or <code>null</null> if not found
	 */
	public Apartment findApartmentStub(Building building, String number);
}
