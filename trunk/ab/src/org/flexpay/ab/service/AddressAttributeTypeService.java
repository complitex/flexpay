package org.flexpay.ab.service;

import org.springframework.security.annotation.Secured;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.List;

public interface AddressAttributeTypeService {

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	AddressAttributeType read(@NotNull Stub<AddressAttributeType> stub);

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	List<AddressAttributeType> getAttributeTypes();

	/**
	 * Create or update building attribute type
	 *
	 * @param type AttributeType to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.BUILDING_ATTRIBUTE_TYPE_ADD, Roles.BUILDING_ATTRIBUTE_TYPE_CHANGE})
	void save(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer;
}
