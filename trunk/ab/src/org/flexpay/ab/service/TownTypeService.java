package org.flexpay.ab.service;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Service interface for TownTypes related tasks
 */
public interface TownTypeService extends MultilangEntityService<TownType, TownTypeTranslation> {

	/**
	 * Initialize filter
	 * 
	 * @param townTypeFilter filter to init
	 * @param locale Locale to get names in
	 * @return initialized filter
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	TownTypeFilter initFilter(TownTypeFilter townTypeFilter, Locale locale) throws FlexPayException;

	/**
	 * Read Entity object by its unique id
	 *
	 * @param stub Entity stub
	 * @return Entity object, or <code>null</code> if object not found
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	@Override
	TownType read(Stub<TownType> stub);

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	@NotNull
	@Override
	List<TownType> getEntities();

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	@Secured (Roles.TOWN_TYPE_DELETE)
	@Override
	void disable(Collection<TownType> entity);

	/**
	 * Disable objects
	 *
	 * @param objectIds IDs of objects to disable
	 */
	@Secured (Roles.TOWN_TYPE_DELETE)
	void disableByIds(@NotNull Collection<Long> objectIds);

	/**
	 * Create Entity
	 *
	 * @param townType Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_TYPE_ADD)
	@Override
	TownType create(@NotNull TownType townType) throws FlexPayExceptionContainer;

	/**
	 * Update or create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_TYPE_CHANGE)
	@Override
	TownType update(@NotNull TownType entity) throws FlexPayExceptionContainer;

}
