package org.flexpay.ab.service;

import java.util.List;
import java.util.Locale;
import java.util.Collection;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.springframework.security.annotation.Secured;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for TownTypes related tasks
 */
public interface TownTypeService extends MultilangEntityService<TownType, TownTypeTranslation> {

	/**
	 * Initialize filter
	 * 
	 * @param townTypeFilter
	 *            filter to init
	 * @param locale
	 *            Locale to get names in
	 * @return initialized filter
	 * @throws FlexPayException
	 *             if failure occurs
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	TownTypeFilter initFilter(TownTypeFilter townTypeFilter, Locale locale)
			throws FlexPayException;

	/**
	 * Read Entity object by its unique id
	 *
	 * @param id Entity key
	 * @return Entity object, or <code>null</code> if object not found
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	TownType read(Long id);

	/**
	 * Get Entity translations for specified locale, if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of Translation
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	List<TownTypeTranslation> getTranslations(Locale locale) throws FlexPayException;

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@Secured (Roles.TOWN_TYPE_READ)
	@NotNull
	List<TownType> getEntities();

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	@Secured (Roles.TOWN_TYPE_DELETE)
	void disable(Collection<TownType> entity);

	/**
	 * Create Entity
	 *
	 * @param townType Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_TYPE_ADD)
	TownType create(@NotNull TownType townType) throws FlexPayExceptionContainer;

	/**
	 * Update or create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.TOWN_TYPE_CHANGE)
	TownType update(@NotNull TownType entity) throws FlexPayExceptionContainer;
}
