package org.flexpay.ab.service;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface IdentityTypeService extends
		MultilangEntityService<IdentityType, IdentityTypeTranslation> {

	/**
	 * Find identity type by enum id
	 *
	 * @param typeId Type id
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@Nullable
	IdentityType getType(int typeId);

	/**
	 * Find identity type by name
	 *
	 * @param typeName Type name
	 * @return IdentityType if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@Nullable
	IdentityType getType(String typeName);

	/**
	 * Read Entity object by its unique id
	 *
	 * @param stub Entity stub
	 * @return Entity object, or <code>null</code> if object not found
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@Override
	IdentityType read(Stub<IdentityType> stub);

	/**
	 * Get a list of available street types
	 *
	 * @return List of Entity
	 */
	@Secured (Roles.IDENTITY_TYPE_READ)
	@NotNull
	@Override
	List<IdentityType> getEntities();

	/**
	 * Disable Entity
	 *
	 * @param entity Entity to disable
	 */
	@Secured (Roles.IDENTITY_TYPE_DELETE)
	@Override
	void disable(Collection<IdentityType> entity);

	/**
	 * Disable objects
	 *
	 * @param objectIds IDs of objects to disable
	 */
	@Secured (Roles.IDENTITY_TYPE_DELETE)
	void disableByIds(@NotNull Collection<Long> objectIds);

	/**
	 * Create Entity
	 *
	 * @param identityType Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.IDENTITY_TYPE_ADD)
	@Override
	IdentityType create(@NotNull IdentityType identityType) throws FlexPayExceptionContainer;

	/**
	 * Update or create Entity
	 *
	 * @param entity Entity to save
	 * @return Saved instance
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.IDENTITY_TYPE_CHANGE)
	@Override
	IdentityType update(@NotNull IdentityType entity) throws FlexPayExceptionContainer;

}
