package org.flexpay.ab.service;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;
import org.apache.commons.collections.ArrayStack;

import java.util.*;

public interface StreetService extends
		NameTimeDependentService<StreetName, StreetNameTemporal, Street, StreetNameTranslation>,
		ParentService<StreetFilter> {

	/**
	 * Save street districts
	 *
	 * @param street	Street to save districts for
	 * @param objectIds List of district ids
	 * @return saved street object
	 */
	@Secured (Roles.STREET_CHANGE)
	Street saveDistricts(Street street, Set<Long> objectIds);

	/**
	 * Lookup streets by name
	 *
	 * @param stub TownStub
	 * @param name Street name search string
	 * @return List of streets with a specified name
	 */
	@Secured (Roles.STREET_READ)
	@NotNull
	List<Street> findByTownAndName(@NotNull Stub<Town> stub, @NotNull String name);

	/**
	 * Save Street types timeline
	 *
	 * @param object Street to update
	 */
	@Secured (Roles.STREET_CHANGE)
	void saveTypes(Street object);

	@Secured (Roles.STREET_READ)
	String format(@NotNull Stub<Street> stub, @NotNull Locale locale, boolean shortMode)
			throws FlexPayException;

	/**
	 * Create or update object
	 *
	 * @param object Object to save
	 */
	@Secured ({Roles.STREET_ADD, Roles.STREET_CHANGE})
	void save(@NotNull Street object);

	/**
	 * Create new NameTimeDependent with a single name
	 *
	 * @param object		   New transient object
	 * @param nameTranslations name translations
	 * @param filters		  parent filters
	 * @param from			 Date from which the name is valid
	 * @return persisted object
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 * @deprecated Use single save for create-update operations
	 */
	@Secured (Roles.STREET_ADD)
	Street create(Street object, List<StreetNameTranslation> nameTranslations, ArrayStack filters, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Secured (Roles.STREET_READ)
	Street readFull(@NotNull Stub<Street> stub);

	/**
	 * Read object temporal name by its unique id
	 *
	 * @param id key
	 * @return object temporal name , or <code>null</code> if not found
	 */
	@Secured (Roles.STREET_READ)
	StreetNameTemporal readTemporalName(Long id);

	/**
	 * Get temporal names
	 *
	 * @param filters parent filters
	 * @param pager   Objects list pager
	 * @return List of names
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.STREET_READ)
	List<StreetName> findNames(ArrayStack filters, Page pager) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Objects
	 */
	@Secured (Roles.STREET_READ)
	List<Street> find(ArrayStack filters);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.STREET_READ)
	List<Street> find(ArrayStack filters, Page pager);

	/**
	 * Disable objects
	 *
	 * @param objects Objects to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Secured (Roles.STREET_DELETE)
	void disable(Collection<Street> objects) throws FlexPayExceptionContainer;

	/**
	 * Update object name translations
	 *
	 * @param obj			  Object to update
	 * @param temporalId	   Temporal id to apply changes for
	 * @param nameTranslations New translations
	 * @param date			 Date from which the name is valid
	 * @return updated object instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          exceptions container
	 */
	@Secured (Roles.STREET_CHANGE)
	Street updateNameTranslations(Street obj, Long temporalId, List<StreetNameTranslation> nameTranslations, Date date)
			throws FlexPayExceptionContainer;

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	@Secured (Roles.STREET_READ)
	StreetNameTranslation getEmptyNameTranslation();

	/**
	 * Find existing object by name
	 *
	 * @param name   Object name to search
	 * @param filter Parent object filter
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.STREET_READ)
	@NotNull
	List<Street> findByName(String name, PrimaryKeyFilter filter);

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
	@Secured (Roles.STREET_READ)
	StreetFilter initFilter(StreetFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale) throws FlexPayException;

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
	@Secured (Roles.STREET_READ)
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;
}
