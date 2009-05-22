package org.flexpay.ab.service;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

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
	 * @param locale		   Locale to get filter translations in
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.STREET_TYPE_READ)
	void initFilter(StreetTypeFilter streetTypeFilter, Locale locale) throws FlexPayException;

	/**
	 * Read Entity object by its unique id
	 *
	 * @param stub Entity stub
	 * @return Entity object, or <code>null</code> if object not found
	 */
	@Secured (Roles.STREET_TYPE_READ)
	StreetType read(Stub<StreetType> stub);

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Secured (Roles.STREET_TYPE_READ)
	StreetType readFull(@NotNull Stub<StreetType> stub);

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@NotNull
	List<StreetType> getEntities();

	/**
	 * Disable Entities
	 *
	 * @param entities Entities to disable
	 */
	@Secured (Roles.STREET_TYPE_DELETE)
	void disable(Collection<StreetType> entities);

	/**
	 * Create Entity
	 *
	 * @param streetType Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.STREET_TYPE_ADD)
	StreetType create(@NotNull StreetType streetType) throws FlexPayExceptionContainer;

	/**
	 * Update or create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.STREET_TYPE_CHANGE)
	StreetType update(@NotNull StreetType entity) throws FlexPayExceptionContainer;
}
