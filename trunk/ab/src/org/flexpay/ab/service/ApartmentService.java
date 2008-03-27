package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;

public interface ApartmentService {

	List<Apartment> getApartments(ArrayStack filters, Page pager);

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	Apartment findApartmentStub(Building building, String number);

	/**
	 * Get apartment number
	 *
	 * @param apartment Apartment, possibly a stub
	 * @return Apartment number
	 * @throws FlexPayException if apartment specified is invalid
	 */
	String getApartmentNumber(Apartment apartment) throws FlexPayException;

	/**
	 * Get building apartment belongs to
	 *
	 * @param apartment Apartment stub
	 * @return Building stub
	 */
	Building getBuilding(Apartment apartment);
}
