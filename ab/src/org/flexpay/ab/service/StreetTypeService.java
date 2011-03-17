package org.flexpay.ab.service;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
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

public interface StreetTypeService extends AllObjectsService<StreetType> {

	/**
	 * Read StreetType object by its unique id
	 *
	 * @param streetTypeStub Street type stub
	 * @return StreetType object, or <code>null</code> if object not found
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@Nullable
	StreetType readFull(@NotNull Stub<StreetType> streetTypeStub);

	/**
	 * Get a list of available street types
	 *
	 * @return List of street types
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@NotNull
	List<StreetType> getEntities();

	/**
	 * Disable street types
	 *
	 * @param streetTypeIds IDs of street types to disable
	 */
	@Secured (Roles.STREET_TYPE_DELETE)
	void disable(@NotNull Collection<Long> streetTypeIds);

	/**
	 * Create street type
	 *
	 * @param streetType Street type to save
	 * @return Saved instance of street type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.STREET_TYPE_ADD)
	@NotNull
	StreetType create(@NotNull StreetType streetType) throws FlexPayExceptionContainer;

	/**
	 * Update or create street type
	 *
	 * @param streetType Street type to save
	 * @return Saved instance of street type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.STREET_TYPE_CHANGE)
	@NotNull
	StreetType update(@NotNull StreetType streetType) throws FlexPayExceptionContainer;

	/**
	 * Find street type by name
	 *
	 * @param typeName type name in any language
	 * @return the first matching type, or <code>null</code> if none matches
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@Nullable
	Stub<StreetType> findTypeByName(@NotNull String typeName) throws FlexPayException;

	/**
	 * Initialize street type filter
	 *
	 * @param streetTypeFilter Filter to init
	 * @param locale		   Locale to get filter translations in
	 * @throws FlexPayException if failure occurs
	 * @return initialized filter
	 */
	@Secured (Roles.STREET_TYPE_READ)
	@NotNull
	StreetTypeFilter initFilter(@Nullable StreetTypeFilter streetTypeFilter, @NotNull Locale locale) throws FlexPayException;

}
