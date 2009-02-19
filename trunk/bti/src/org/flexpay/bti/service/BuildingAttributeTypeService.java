package org.flexpay.bti.service;

import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BuildingAttributeTypeService {

	/**
	 * Read full type info
	 *
	 * @param stub Attribute type stub
	 * @return type if found, or <code>null</code> otherwise
	 */
	@Nullable
	BuildingAttributeType readFull(@NotNull Stub<BuildingAttributeType> stub);

	/**
	 * Create a new attribute type
	 *
	 * @param type Attribute type to create
	 * @return persisted type back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	BuildingAttributeType create(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Update existing attribute type
	 *
	 * @param type Attribute type to update
	 * @return type back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	BuildingAttributeType update(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Find building attribute types
	 *
	 * @param pager Page
	 * @return list of building attributes
	 */
	List<BuildingAttributeType> listTypes(@NotNull Page<BuildingAttributeType> pager);

    /**
	 * Find building attribute types
	 *
	 * @return list of building attributes
	 */
	List<BuildingAttributeType> listTypes();

	/**
	 * Find attribute type by name
	 * 
	 * @param typeName Type name to look up
	 * @return type if found, or <code>null</code> otherwise
	 */
	@Nullable
	BuildingAttributeType findTypeByName(String typeName);

}
