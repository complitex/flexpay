package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.NameTimeDependentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface StreetService extends NameTimeDependentService<StreetName, StreetNameTemporal, Street, StreetNameTranslation> {

	/**
	 * Read streets collection by theirs ids
	 *
 	 * @param streetIds Street ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found streets
	 */
	@Secured ({Roles.STREET_READ})
	@NotNull
	List<Street> readFull(@NotNull Collection<Long> streetIds, boolean preserveOrder);

	/**
	 * Disable streets
	 *
	 * @param streetIds IDs of streets to disable
	 */
	@Secured (Roles.STREET_DELETE)
	void disable(@NotNull Collection<Long> streetIds);

	/**
	 * Create street
	 *
	 * @param street Street to save
	 * @return Saved instance of street
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.STREET_ADD)
	@NotNull
	Street create(@NotNull Street street) throws FlexPayExceptionContainer;

	/**
	 * Update or create street
	 *
	 * @param street Street to save
	 * @return Saved instance of street
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.STREET_CHANGE)
	@NotNull
	Street update(@NotNull Street street) throws FlexPayExceptionContainer;

	/**
	 * Read street with its full hierarchical structure:
	 * country-region-town
	 *
	 * @param streetStub Street stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.STREET_READ})
	@Nullable
	Street readWithHierarchy(@NotNull Stub<Street> streetStub);

	/**
	 * Lookup streets by query and town id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * street_type street_name
	 *
	 * @param parentStub Town stub
	 * @param query searching string
	 * @return List of found streets
	 */
	@Secured (Roles.STREET_READ)
	@NotNull
	List<Street> findByParentAndQuery(@NotNull Stub<Town> parentStub, @NotNull String query);

	/**
	 * Lookup streets by query and town id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * street_type street_name
	 *
	 * @param parentStub Town stub
	 * @param query searching string
	 * @param sorters sorters
	 * @param language language for search
	 * @param pager pager
	 * @return List of found streets
	 */
	@Secured (Roles.STREET_READ)
	@NotNull
	List<Street> findByParentAndQuery(@NotNull Stub<Town> parentStub, @NotNull List<? extends ObjectSorter> sorters,
									  @NotNull String query, @NotNull Language language, @NotNull Page<Street> pager);

	/**
	 * Lookup streets by name, street type and town
	 *
	 * @param townStub Town stub
	 * @param name Street name search string
	 * @param typeStub Street type stub
	 * @return List of found streets
	 */
	@Secured (Roles.STREET_READ)
	@NotNull
	List<Street> findByTownAndNameAndType(@NotNull Stub<Town> townStub, @NotNull String name, @NotNull Stub<StreetType> typeStub);

	/**
	 * Save street districts
	 *
	 * @param street	Street to save districts for
	 * @param districtIds List of district ids
	 * @return saved street object
	 * @throws FlexPayExceptionContainer if street validation fails
	 */
	@Secured (Roles.STREET_CHANGE)
	@NotNull
	Street saveDistricts(@NotNull Street street, @NotNull Set<Long> districtIds) throws FlexPayExceptionContainer;

	/**
	 * List all districts the street lays in
	 *
	 * @param streetStub Street stub
	 * @return List of districts
	 */
	@NotNull
	@Secured (Roles.DISTRICT_READ)
	List<District> getStreetDistricts(@NotNull Stub<Street> streetStub);

	@Secured (Roles.STREET_READ)
	@NotNull
	String format(@NotNull Stub<Street> streetStub, @NotNull Locale locale, boolean shortMode) throws FlexPayException;

	/**
	 * Find street ids for town in range
	 *
	 * @param townStub Town to get streets for
	 * @param range FetchRange
	 * @return List of streets in range
	 */
	@Secured (Roles.STREET_READ)
	@NotNull
	List<Street> findSimpleByTown(Stub<Town> townStub, FetchRange range);

    /**
     * Find street by filters
     *
     * @param filters filter stack
     * @return Street
     */
    @Secured (Roles.STREET_READ)
    @Nullable
    Street findStreet(ArrayStack filters);

}
