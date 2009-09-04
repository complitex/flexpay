package org.flexpay.bti.persistence.building;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

/**
 * Simple attribute type can have every value
 */
public class BuildingAttributeTypeSimple extends BuildingAttributeType {

	public BuildingAttributeTypeSimple() {
	}

	public BuildingAttributeTypeSimple(@NotNull Long id) {
		super(id);
	}

	public BuildingAttributeTypeSimple(@NotNull Stub<BuildingAttributeType> stub) {
		super(stub.getId());
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if validation fails
	 */
	@Override
	public void validate(String value) throws FlexPayException {
		// do nothing, every value is OK
	}

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	@Override
	public String getI18nTitle() {
		return "bti.building.attribute.type.simple";
	}

}
