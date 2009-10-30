package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Town service
 */
public interface TownService extends NameTimeDependentService<
		TownName, TownNameTemporal, Town, TownNameTranslation>,
		ParentService<TownFilter> {

	/**
	 * Create Town object
	 *
	 * @param town Town object to save
	 * @return saved object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_ADD)
	Town create(@NotNull Town town) throws FlexPayExceptionContainer;

	/**
	 * Update Town object
	 *
	 * @param town Town object to save
	 * @return saved object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_CHANGE)
	Town update(@NotNull Town town) throws FlexPayExceptionContainer;

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
	@Secured (Roles.TOWN_READ)
	@Override
	TownFilter initFilter(TownFilter parentFilter, PrimaryKeyFilter<?> forefatherFilter, Locale locale) throws FlexPayException;

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
	@Secured (Roles.TOWN_READ)
	@Override
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
	@Secured (Roles.TOWN_ADD)
	@Override
	Town create(Town object, List<TownNameTranslation> nameTranslations, ArrayStack filters, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Secured (Roles.TOWN_READ)
	@Nullable
	@Override
	Town readFull(@NotNull Stub<Town> stub);

	/**
	 * Read towns
	 *
	 * @param stubs		 town keys
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Objects if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.TOWN_READ})
	@NotNull
	List<Town> readFull(@NotNull Collection<Long> stubs, boolean preserveOrder);

	/**
	 * Read object temporal name by its unique id
	 *
	 * @param id key
	 * @return object temporal name , or <code>null</code> if not found
	 */
	@Secured (Roles.TOWN_READ)
	@Override
	TownNameTemporal readTemporalName(Long id);

	/**
	 * Get temporal names
	 *
	 * @param filters parent filters
	 * @param pager   Objects list pager
	 * @return List of names
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.TOWN_READ)
	@Override
	List<TownName> findNames(ArrayStack filters, Page<Town> pager) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Objects
	 */
	@Secured (Roles.TOWN_READ)
	@Override
	List<Town> find(ArrayStack filters);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.TOWN_READ)
	@Override
	List<Town> find(ArrayStack filters, Page<Town> pager);

	/**
	 * Lookup streets by query and region id. Query is a string which may contains in folow string:
	 * <p/>
	 * town_name
	 *
	 * @param stub  RegionStub
	 * @param query searching string
	 * @return List of founded towns
	 */
	@Secured (Roles.TOWN_READ)
	@NotNull
	List<Town> findByRegionAndQuery(@NotNull Stub<Region> stub, @NotNull String query);

	/**
	 * Disable objects
	 *
	 * @param objects Objects to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Secured (Roles.TOWN_DELETE)
	@Override
	void disable(Collection<Town> objects) throws FlexPayExceptionContainer;

	/**
	 * Disable objects
	 *
	 * @param objectIds IDs of objects to disable
	 */
	@Secured (Roles.TOWN_DELETE)
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
	@Secured (Roles.TOWN_CHANGE)
	@Override
	Town updateNameTranslations(Town obj, Long temporalId, List<TownNameTranslation> nameTranslations, Date date)
			throws FlexPayExceptionContainer;

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	@Secured (Roles.TOWN_READ)
	@Override
	TownNameTranslation getEmptyNameTranslation();

	/**
	 * Find existing object by name
	 *
	 * @param name   Object name to search
	 * @param filter Parent object filter
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.TOWN_READ)
	@NotNull
	@Override
	List<Town> findByName(String name, PrimaryKeyFilter<?> filter);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.TOWN_READ)
	List<Town> find(ArrayStack filters, List<ObjectSorter> sorters, Page<Town> pager);

}
