package org.flexpay.ab.service;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.Locale;
import java.util.List;
import java.util.Collection;

public interface StreetTypeService extends
		MultilangEntityService<StreetType, StreetTypeTranslation> {

	/**
	 * Find street type by name
	 *
	 * @param typeName type name in any language
	 * @return the first matching type, or <code>null</code> if none matches
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@Nullable
	StreetType findTypeByName(@NotNull String typeName) throws FlexPayException;

	/**
	 * Initialize street type filter
	 *
	 * @param streetTypeFilter Filter to init
	 * @param locale Locale to get filter translations in
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.STREET_TYPE_READ)
	void initFilter(StreetTypeFilter streetTypeFilter, Locale locale) throws FlexPayException;

	/**
	 * Read Entity object by its unique id
	 *
	 * @param id Entity key
	 * @return Entity object, or <code>null</code> if object not found
	 */
	@Secured (Roles.STREET_TYPE_READ)
	StreetType read(Long id);

	/**
	 * Get Entity translations for specified locale, if translation is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of Translation
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.STREET_TYPE_READ)
	List<StreetTypeTranslation> getTranslations(Locale locale) throws FlexPayException;

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@NotNull
	List<StreetType> getEntities();

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	@Secured (Roles.STREET_TYPE_DELETE)
	void disable(Collection<StreetType> entity);

	/**
	 * Update or create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured ({Roles.STREET_TYPE_ADD, Roles.STREET_TYPE_CHANGE})
	StreetType save(@NotNull StreetType entity) throws FlexPayExceptionContainer;
}
