package org.flexpay.ab.service;

import org.springframework.security.annotation.Secured;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.List;

public interface BuildingAttributeTypeService {

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAttributeType read(@NotNull Stub<BuildingAttributeType> stub);

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	List<BuildingAttributeType> getAttributeTypes();

	/**
	 * Create or update building attribute type
	 *
	 * @param type AttributeType to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.BUILDING_ATTRIBUTE_TYPE_ADD, Roles.BUILDING_ATTRIBUTE_TYPE_CHANGE})
	void save(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer;
}
