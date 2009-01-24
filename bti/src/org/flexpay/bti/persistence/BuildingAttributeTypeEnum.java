package org.flexpay.bti.persistence;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.exception.FlexPayException;
import org.apache.commons.lang.StringUtils;

import java.util.Set;
import java.util.Collections;

public class BuildingAttributeTypeEnum extends BuildingAttributeType {

	private Set<BuildingAttributeTypeEnumValue> values = Collections.emptySet();

	public Set<BuildingAttributeTypeEnumValue> getValues() {
		return values;
	}

	public void setValues(Set<BuildingAttributeTypeEnumValue> values) {
		this.values = values;
	}

	public void addValue(BuildingAttributeTypeEnumValue value) {
		if (values == Collections.EMPTY_SET) {
			values = CollectionUtils.set();
		}

		values.add(value);
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if validation fails
	 */
	void validate(String value) throws FlexPayException {

		if(StringUtils.isBlank(value)) {
			throw new FlexPayException("No blank value allowed", "bti.error.building.attribute.enum.blank_value");
		}

		for (BuildingAttributeTypeEnumValue enumValue : values) {
			if (value.equals(enumValue.getValue())) {
				return;
			}
		}

		throw new FlexPayException("No match", "bti.error.building.attribute.enum.no_match");
	}
}
