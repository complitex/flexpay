package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ApartmentService extends ParentService<ApartmentFilter> {

	List<Apartment> getApartments(ArrayStack filters, Page pager);

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number   Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Nullable
	Stub<Apartment> findApartmentStub(@NotNull Building building, String number);

	/**
	 * Get apartment number
	 *
	 * @param stub Apartment stub
	 * @return Apartment number
	 * @throws FlexPayException if apartment specified is invalid
	 */
	String getApartmentNumber(Stub<Apartment> stub) throws FlexPayException;

	/**
	 * Get building apartment belongs to
	 *
	 * @param apartment Apartment stub
	 * @return Building stub
	 * @throws FlexPayException if stub references invalid object
	 */
	@NotNull
	Building getBuilding(Stub<Apartment> apartment) throws FlexPayException;

	/**
	 * Create or update apartment
	 *
	 * @param apartment Apartment to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(Apartment apartment) throws FlexPayExceptionContainer;

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
	 * Read apartment with registered persons
	 *
	 * @param stub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	Apartment readWithPersons(@NotNull Stub<Apartment> stub);

	void fillFilterIds(@NotNull Stub<Apartment> stub, ArrayStack filters) throws FlexPayException;

	/**
	 * Disable apartments
	 *
	 * @param objectIds Apartments identifiers
	 */
	void disable(@NotNull Set<Long> objectIds);
}
