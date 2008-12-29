package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;
import java.util.Locale;

public interface ApartmentService extends ParentService<ApartmentFilter> {

	@Secured (Roles.APARTMENT_READ)
	List<Apartment> getApartments(ArrayStack filters, Page pager);

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number   Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.APARTMENT_READ)
	@Nullable
	Stub<Apartment> findApartmentStub(@NotNull Building building, String number);

	/**
	 * Get apartment number
	 *
	 * @param stub Apartment stub
	 * @return Apartment number
	 * @throws FlexPayException if apartment specified is invalid
	 */
	@Secured (Roles.APARTMENT_READ)
	String getApartmentNumber(Stub<Apartment> stub) throws FlexPayException;

	/**
	 * Get building apartment belongs to
	 *
	 * @param apartment Apartment stub
	 * @return Building stub
	 * @throws FlexPayException if stub references invalid object
	 */
	@Secured (Roles.APARTMENT_READ)
	@NotNull
	Building getBuilding(Stub<Apartment> apartment) throws FlexPayException;

	/**
	 * Create or update apartment
	 *
	 * @param apartment Apartment to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.APARTMENT_ADD, Roles.APARTMENT_CHANGE})
	void save(Apartment apartment) throws FlexPayExceptionContainer;

	/**
	 * Get apartment display address
	 *
	 * @param stub Apartment stub
	 * @return Apartment address
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.APARTMENT_READ)
	@NotNull
	public String getAddress(@NotNull Stub<Apartment> stub) throws FlexPayException;

	/**
	 * Read apartment with registered persons
	 *
	 * @param stub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.APARTMENT_READ, Roles.PERSON_READ})
	@Nullable
	Apartment readWithPersons(@NotNull Stub<Apartment> stub);

	@Secured (Roles.APARTMENT_READ)
	void fillFilterIds(@NotNull Stub<Apartment> stub, ArrayStack filters) throws FlexPayException;

	/**
	 * Disable apartments
	 *
	 * @param objectIds Apartments identifiers
	 */
	@Secured (Roles.APARTMENT_DELETE)
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.APARTMENT_READ)
	ApartmentFilter initFilter(ApartmentFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException;

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less significant ones order, like
	 * CountryFilter, RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.APARTMENT_READ)
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;
}
