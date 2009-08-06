package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.RegionFilter;
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

public interface RegionService extends
		ParentService<RegionFilter>,
		NameTimeDependentService<RegionName, RegionNameTemporal, Region, RegionNameTranslation> {

	/**
	 * Create Region object
	 *
	 * @param region Region object to save
	 * @return saved object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.REGION_ADD)
	Region create(@NotNull Region region) throws FlexPayExceptionContainer;

	/**
	 * Update Region object
	 *
	 * @param region Region object to save
	 * @return saved object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.REGION_CHANGE)
	Region update(@NotNull Region region) throws FlexPayExceptionContainer;

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
	@Secured (Roles.REGION_READ)
	RegionFilter initFilter(RegionFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException;

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less significant ones order, like
	 * CountryFilter, RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.REGION_READ)
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;

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
	@Secured (Roles.REGION_ADD)
	Region create(Region object, List<RegionNameTranslation> nameTranslations, ArrayStack filters, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Secured (Roles.REGION_READ)
	Region readFull(@NotNull Stub<Region> stub);

	/**
	 * Read regions
	 *
	 * @param stubs		 Region keys
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Objects if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.REGION_READ})
	@NotNull
	List<Region> readFull(@NotNull Collection<Long> stubs, boolean preserveOrder);

	/**
	 * Read object temporal name by its unique id
	 *
	 * @param id key
	 * @return object temporal name , or <code>null</code> if not found
	 */
	@Secured (Roles.REGION_READ)
	RegionNameTemporal readTemporalName(Long id);

	/**
	 * Get temporal names
	 *
	 * @param filters parent filters
	 * @param pager   Objects list pager
	 * @return List of names
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.REGION_READ)
	List<RegionName> findNames(ArrayStack filters, Page pager) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Objects
	 */
	@Secured (Roles.REGION_READ)
	List<Region> find(ArrayStack filters);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.REGION_READ)
	List<Region> find(ArrayStack filters, Page pager);

	/**
	 * Disable objects
	 *
	 * @param objects Objects to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Secured (Roles.REGION_DELETE)
	void disable(Collection<Region> objects) throws FlexPayExceptionContainer;

	/**
	 * Disable objects
	 *
	 * @param objectIds IDs of objects to disable
	 */
	@Secured (Roles.REGION_DELETE)
	void disableByIds(@NotNull Collection<Long> objectIds);

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
	@Secured (Roles.REGION_CHANGE)
	Region updateNameTranslations(Region obj, Long temporalId, List<RegionNameTranslation> nameTranslations, Date date)
			throws FlexPayExceptionContainer;

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	RegionNameTranslation getEmptyNameTranslation();

	/**
	 * Find existing object by name
	 *
	 * @param name   Object name to search
	 * @param filter Parent object filter
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.REGION_READ)
	@NotNull
	List<Region> findByName(String name, PrimaryKeyFilter filter);

	/**
	 * Lookup regions by query and country id. Query is a string which may contains in folow string:
	 * <p/>
	 * region_name
	 *
	 * @param stub  CountryStub
	 * @param query searching string
	 * @return List of founded regions
	 */
	@Secured (Roles.REGION_READ)
	@NotNull
	List<Region> findByCountryAndQuery(@NotNull Stub<Country> stub, @NotNull String query);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.REGION_READ)
	List<Region> find(ArrayStack filters, List<ObjectSorter> sorters, Page<Region> pager);
}
