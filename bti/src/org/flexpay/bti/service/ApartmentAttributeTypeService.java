package org.flexpay.bti.service;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ApartmentAttributeTypeService {

	/**
	 * Read full type info
	 *
	 * @param stub Attribute type stub
	 * @return type if found, or <code>null</code> otherwise
	 */
	@Nullable
	ApartmentAttributeType readFull(@NotNull Stub<ApartmentAttributeType> stub);

	/**
	 * Create a new attribute type
	 *
	 * @param type Attribute type to create
	 * @return persisted type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
	 */
	ApartmentAttributeType create(@NotNull ApartmentAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Update existing attribute type
	 *
	 * @param type Attribute type to update
	 * @return type back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer if validation fails
	 */
	ApartmentAttributeType update(@NotNull ApartmentAttributeType type) throws FlexPayExceptionContainer;

	/**
	 * Find apartment attribute types
	 *
	 * @param pager Page
	 * @return list of apartment attributes
	 */
	List<ApartmentAttributeType> listTypes(@NotNull Page<ApartmentAttributeType> pager);

    /**
	 * Find apartment attribute types
	 *
	 * @return list of apartment attributes
	 */
	List<ApartmentAttributeType> findTypes();

	/**
	 * Find attribute type by name
	 *
	 * @param typeName Type name to look up
	 * @return type if found, or <code>null</code> otherwise
	 */
	@Nullable
	ApartmentAttributeType findTypeByName(String typeName);

}
