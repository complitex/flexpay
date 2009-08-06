package org.flexpay.ab.service;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Collection;

public interface AddressAttributeTypeService {

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	@Nullable
	AddressAttributeType read(@NotNull Stub<AddressAttributeType> stub);

	/**
	 * Get building attribute types sorted using AddressAttributeType natural comparator
	 *
	 * @return BuildingAttributeType list
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	List<AddressAttributeType> getAttributeTypes();

	/**
	 * Create building address attribute type
	 *
	 * @param type AttributeType to save
	 * @return persisted object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_ADD)
	@NotNull
	AddressAttributeType create(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Update building attribute type
	 *
	 * @param type AttributeType to save
	 * @return updated object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_CHANGE)
	@NotNull
	AddressAttributeType update(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Disable Entities
	 *
	 * @param entities Entities to disable
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_DELETE)
	void disable(Collection<AddressAttributeType> entities);
}
