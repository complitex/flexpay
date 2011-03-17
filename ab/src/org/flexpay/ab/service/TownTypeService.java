package org.flexpay.ab.service;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.AllObjectsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Service interface for TownTypes related tasks
 */
public interface TownTypeService extends AllObjectsService<TownType> {

	/**
	 * Read TownType object by its unique id
	 *
	 * @param stub Town type stub
	 * @return TownType object, or <code>null</code> if object not found
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	@Nullable
	TownType readFull(@NotNull Stub<TownType> stub);

	/**
	 * Get a list of available town types
	 *
	 * @return List of TownTypes
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	@NotNull
	List<TownType> getEntities();

	/**
	 * Disable town types
	 *
	 * @param townTypeIds IDs of town types to disable
	 */
	@Secured (Roles.TOWN_TYPE_DELETE)
	void disable(@NotNull Collection<Long> townTypeIds);

	/**
	 * Create town type
	 *
	 * @param townType Town type to save
	 * @return Saved instance of town type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_TYPE_ADD)
	@NotNull
	TownType create(@NotNull TownType townType) throws FlexPayExceptionContainer;

	/**
	 * Update or create town type
	 *
	 * @param townType Town type to save
	 * @return Saved instance of town type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_TYPE_CHANGE)
	@NotNull
	TownType update(@NotNull TownType townType) throws FlexPayExceptionContainer;

	/**
	 * Initialize filter
	 *
	 * @param townTypeFilter filter to init
	 * @param locale Locale to get names in
	 * @return initialized filter
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	@NotNull
	TownTypeFilter initFilter(@Nullable TownTypeFilter townTypeFilter, @NotNull Locale locale) throws FlexPayException;

}
