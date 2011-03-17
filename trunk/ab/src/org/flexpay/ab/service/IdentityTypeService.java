package org.flexpay.ab.service;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.AllObjectsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface IdentityTypeService extends AllObjectsService<IdentityType> {

	/**
	 * Read IdentityType object by its unique id
	 *
	 * @param stub Identity type stub
	 * @return IdentityType object, or <code>null</code> if object not found
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@Nullable
	IdentityType readFull(@NotNull Stub<IdentityType> stub);

	/**
	 * Get a list of available identity types
	 *
	 * @return List of identity types
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@NotNull
	List<IdentityType> getEntities();

	/**
	 * Disable identity types
	 *
	 * @param identityTypeIds IDs of identity types to disable
	 */
	@Secured (Roles.IDENTITY_TYPE_DELETE)
	void disable(@NotNull Collection<Long> identityTypeIds);

	/**
	 * Create identity type
	 *
	 * @param identityType Identity type to save
	 * @return Saved instance of identity type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.IDENTITY_TYPE_ADD)
	@NotNull
	IdentityType create(@NotNull IdentityType identityType) throws FlexPayExceptionContainer;

	/**
	 * Update or create identity type
	 *
	 * @param identityType Identity type to save
	 * @return Saved instance of identity type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.IDENTITY_TYPE_CHANGE)
	@NotNull
	IdentityType update(@NotNull IdentityType identityType) throws FlexPayExceptionContainer;

	/**
	 * Find identity type by name
	 *
	 * @param typeName Identity type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@Nullable
	IdentityType findTypeByName(@Nullable String typeName);

	/**
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@Nullable
	IdentityType findTypeById(int typeId);

}
