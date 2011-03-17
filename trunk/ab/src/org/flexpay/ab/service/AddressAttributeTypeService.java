package org.flexpay.ab.service;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.AllObjectsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;

public interface AddressAttributeTypeService extends AllObjectsService<AddressAttributeType> {

	/**
	 * Read AddressAttributeType object by its unique id
	 *
	 * @param stub Address attribute type stub
	 * @return AddressAttributeType object, or <code>null</code> if object not found
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	@Nullable
	AddressAttributeType readFull(@NotNull Stub<AddressAttributeType> stub);

	/**
	 * Get a list of available address attribute types
	 * sorted using AddressAttributeType natural comparator
	 *
	 * @return List of address attribute types
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	@NotNull
	List<AddressAttributeType> getAttributeTypes();

	/**
	 * Disable address attribute types
	 *
	 * @param addressAttributeTypeIds IDs of address attribute types to disable
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_DELETE)
	void disable(@NotNull Collection<Long> addressAttributeTypeIds);

	/**
	 * Create address attribute type
	 *
	 * @param type Address attribute type to save
	 * @return Saved instance of address attribute type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_ADD)
	@NotNull
	AddressAttributeType create(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Update or create address attribute type
	 *
	 * @param type Address attribute type to save
	 * @return Saved instance of address attribute type
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_CHANGE)
	@NotNull
	AddressAttributeType update(@NotNull AddressAttributeType type) throws FlexPayExceptionContainer;

}
