package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.ObjectAlreadyExistException;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ApartmentService {

	List<Apartment> getApartments(ArrayStack filters, Page pager);

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number   Apartment number
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

	/**
	 * Read full apartment information
	 *
	 * @param id Apartment id
	 * @return Apartment instance, or <code>null</code> if not found
	 */
	Apartment readFull(Long id);


	/**
	 * Validate that given number not alredy exist in given apartment's building. If not exist then set new number.
	 *
	 * @param apartment Apartment
	 * @param number	apartment number
	 * @throws ObjectAlreadyExistException if given number alredy exists in a building.
	 */
	void setApartmentNumber(Apartment apartment, String number) throws ObjectAlreadyExistException;

	/**
	 * Get apartment display address
	 *
	 * @param stub Apartment stub
	 * @return Apartment address
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	public String getAddress(@NotNull Stub<Apartment> stub) throws FlexPayException;

	/**
	 * Create a new apartment
	 *
	 * @param apartment Apartment object
	 */
	void create(Apartment apartment);

	/**
	 * Read apartment with registered persons
	 *
	 * @param id Object identifier
	 * @return Object if found, or <code>null</code> otherwise
	 */
	Apartment readWithPersons(Long id);

	void fillFilterIds(@NotNull Apartment apartment, CountryFilter countryFilter, RegionFilter regionFilter, TownFilter townFilter, StreetFilter streetFilter, BuildingsFilter buildingsFilter);
}
