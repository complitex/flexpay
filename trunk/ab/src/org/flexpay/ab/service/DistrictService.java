package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public interface DistrictService extends NameTimeDependentService<
		DistrictName, DistrictNameTemporal, District, DistrictNameTranslation>,
		ParentService<DistrictFilter> {

	/**
	 * Create District object
	 *
	 * @param district District object to save
	 * @return saved object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.DISTRICT_ADD)
	District create(@NotNull District district) throws FlexPayExceptionContainer;

	/**
	 * Create or update Town object
	 *
	 * @param district District object to save
	 * @return saved object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.DISTRICT_CHANGE)
	District update(@NotNull District district) throws FlexPayExceptionContainer;

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
	 */
	@Secured (Roles.DISTRICT_ADD)
	District create(District object, List<DistrictNameTranslation> nameTranslations, ArrayStack filters, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Secured (Roles.DISTRICT_READ)
	District readFull(@NotNull Stub<District> stub);

	/**
	 * Read districts
	 *
	 * @param stubs		 district keys
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Objects if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.DISTRICT_READ})
	@NotNull
	List<District> readFull(@NotNull Collection<Long> stubs, boolean preserveOrder);

	/**
	 * Read object temporal name by its unique id
	 *
	 * @param id key
	 * @return object temporal name , or <code>null</code> if not found
	 */
	@Secured (Roles.DISTRICT_READ)
	DistrictNameTemporal readTemporalName(Long id);

	/**
	 * Get temporal names
	 *
	 * @param filters parent filters
	 * @param pager   Objects list pager
	 * @return List of names
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.DISTRICT_READ)
	List<DistrictName> findNames(ArrayStack filters, Page pager) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Objects
	 */
	@Secured (Roles.DISTRICT_READ)
	List<District> find(ArrayStack filters);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.DISTRICT_READ)
	List<District> find(ArrayStack filters, Page pager);

	/**
	 * Disable objects
	 *
	 * @param objects Objects to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Secured (Roles.DISTRICT_DELETE)
	void disable(Collection<District> objects) throws FlexPayExceptionContainer;

	/**
	 * Disable objects
	 *
	 * @param objectIds IDs of objects to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Secured (Roles.DISTRICT_DELETE)
	void disableByIds(@NotNull Collection<Long> objectIds) throws FlexPayExceptionContainer;

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
	@Secured (Roles.DISTRICT_CHANGE)
	District updateNameTranslations(District obj, Long temporalId, List<DistrictNameTranslation> nameTranslations, Date date)
			throws FlexPayExceptionContainer;

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	@Secured (Roles.DISTRICT_READ)
	DistrictNameTranslation getEmptyNameTranslation();

	/**
	 * Find existing object by name
	 *
	 * @param name   Object name to search
	 * @param filter Parent object filter
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.DISTRICT_READ)
	@NotNull
	List<District> findByName(String name, PrimaryKeyFilter filter);

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
	@Secured (Roles.DISTRICT_READ)
	DistrictFilter initFilter(DistrictFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale) throws FlexPayException;

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
	@Secured (Roles.DISTRICT_READ)
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;


	/**
	 * Lookup districts by query and town id. Query is a string which may contains in folow string:
	 * <p/>
	 * district_name
	 *
	 * @param stub  TownStub
	 * @param query searching string
	 * @return List of founded districts
	 */
	@Secured (Roles.DISTRICT_READ)
	@NotNull
	List<District> findByTownAndQuery(@NotNull Stub<Town> stub, @NotNull String query);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured(Roles.DISTRICT_READ)
	List<District> find(ArrayStack filters, List<ObjectSorter> sorters, Page<District> pager);
}
