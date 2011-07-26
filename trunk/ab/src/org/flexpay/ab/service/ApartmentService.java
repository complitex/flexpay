package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface ApartmentService extends JpaSetService {

	/**
	 * Read apartment
	 *
	 * @param apartmentStub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.APARTMENT_READ})
	@Nullable
	Apartment readFull(@NotNull Stub<Apartment> apartmentStub);

    /**
     * Read apartment with full hirarchy and names
     *
     * @param apartmentStub Apartment stub
     * @return Object if found, or <code>null</code> otherwise
     */
    @Secured ({Roles.APARTMENT_READ})
    @Nullable
    Apartment readFullWithHierarchy(@NotNull Stub<Apartment> apartmentStub);

	/**
	 * Read apartments collection by theirs ids
	 *
 	 * @param apartmentIds Apartment ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found apartments
	 */
	@Secured ({Roles.APARTMENT_READ})
	@NotNull
	List<Apartment> readFull(@NotNull Collection<Long> apartmentIds, boolean preserveOrder);

	/**
	 * Disable apartments
	 *
	 * @param apartmentIds IDs of apartments to disable
	 */
	@Secured (Roles.APARTMENT_DELETE)
	void disable(@NotNull Collection<Long> apartmentIds);

	/**
	 * Create apartment
	 *
	 * @param apartment Apartment to save
	 * @return Saved instance of apartment
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.APARTMENT_ADD)
	@NotNull
	Apartment create(@NotNull Apartment apartment) throws FlexPayExceptionContainer;

	/**
	 * Update or create apartment
	 *
	 * @param apartment Apartment to save
	 * @return Saved instance of apartment
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.APARTMENT_CHANGE)
	@NotNull
	Apartment update(@NotNull Apartment apartment) throws FlexPayExceptionContainer;

	/**
	 * Read apartment with registered persons
	 *
	 * @param stub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.APARTMENT_READ, Roles.PERSON_READ})
	@NotNull
	Apartment readWithPersons(@NotNull Stub<Apartment> stub);

	/**
	 * Read apartment with its full hierarchical structure:
	 * country-region-street-building
	 *
	 * @param apartmentStub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.APARTMENT_READ})
	@Nullable
	Apartment readWithHierarchy(@NotNull Stub<Apartment> apartmentStub);

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number   Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.APARTMENT_READ)
	@Nullable
	Stub<Apartment> findApartmentStub(@NotNull Building building, @NotNull String number);

	/**
	 * Get a list of available apartments
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.APARTMENT_READ)
	@NotNull
	List<Apartment> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Apartment> pager);

	/**
	 * Get a list of available apartments
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.APARTMENT_READ)
	@NotNull
	List<Apartment> find(@NotNull ArrayStack filters, Page<Apartment> pager);

	/**
	 * Lookup apartments by building address id.
	 *
	 * @param addressStub  Building address stub
	 * @return List of found apartments
	 */
	@Secured (Roles.APARTMENT_READ)
	@NotNull
	List<Apartment> findByParent(@NotNull Stub<BuildingAddress> addressStub);

	/**
	 * Get apartment number
	 *
	 * @param apartmentStub Apartment stub
	 * @return Apartment number
	 * @throws FlexPayException if apartment specified is invalid
	 */
	@Secured (Roles.APARTMENT_READ)
	@Nullable
	String getApartmentNumber(@NotNull Stub<Apartment> apartmentStub) throws FlexPayException;

	/**
	 * Find apartments ids for town in range
	 *
	 * @param townStub Town to get apartments for
	 * @param range FetchRange
	 * @return List of apartments in range
	 */
	List<Apartment> findSimpleByTown(Stub<Town> townStub, FetchRange range);
}
