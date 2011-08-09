package org.flexpay.bti.persistence.building;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import static org.flexpay.common.util.CollectionUtils.set;

public class BuildingAttributeTypeEnum extends BuildingAttributeType {

	private Set<BuildingAttributeTypeEnumValue> values = set();

	public BuildingAttributeTypeEnum() {
	}

	public BuildingAttributeTypeEnum(@NotNull Long id) {
		super(id);
	}

	public BuildingAttributeTypeEnum(@NotNull Stub<BuildingAttributeType> stub) {
		super(stub);
	}

	public Set<BuildingAttributeTypeEnumValue> getValues() {
		return values;
	}

	public SortedSet<BuildingAttributeTypeEnumValue> getSortedValues() {
		return CollectionUtils.treeSet(getValues());
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setValues(Set<BuildingAttributeTypeEnumValue> values) {
		this.values = values;
	}

	@SuppressWarnings ({"CollectionsFieldAccessReplaceableByMethodCall"})
	public void addValue(BuildingAttributeTypeEnumValue value) {
		if (values == null) {
			values = set();
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
	@Override
	public void validate(String value) throws FlexPayException {

		if (StringUtils.isBlank(value)) {
			throw new FlexPayException("No blank value allowed", "bti.error.building.attribute.enum.blank_value");
		}

		for (BuildingAttributeTypeEnumValue enumValue : values) {
			if (value.equals(enumValue.getValue())) {
				return;
			}
		}

		throw new FlexPayException("No match", "bti.error.building.attribute.enum.no_match");
	}

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	@Override
	public String getI18nTitle() {
		return "bti.building.attribute.type.enum";
	}

	/**
	 * Setup new enum values
	 *
	 * @param enumValues Order to value map to set up
	 */
	public void rawValues(Map<Integer, String> enumValues) {

		Map<Integer, String> newValues = CollectionUtils.map(enumValues);
		Map<Integer, BuildingAttributeTypeEnumValue> valuesMap = CollectionUtils.map();
		for (BuildingAttributeTypeEnumValue oldValue : values) {
			valuesMap.put(oldValue.getOrder(), oldValue);
		}

		for (Map.Entry<Integer, BuildingAttributeTypeEnumValue> entry : valuesMap.entrySet()) {
			if (newValues.containsKey(entry.getKey())) {
				// value with this order found, just update string value
				entry.getValue().setValue(newValues.get(entry.getKey()));
				newValues.remove(entry.getKey());
			} else {
				// no value with this order, remove enum value
				values.remove(entry.getValue());
			}
		}

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (values == null) {
			values = set();
		}

		// now create all new values
		for (Map.Entry<Integer, String> entry : newValues.entrySet()) {
			BuildingAttributeTypeEnumValue value = new BuildingAttributeTypeEnumValue();
			value.setOrder(entry.getKey());
			value.setValue(entry.getValue());
			value.setTypeEnum(this);
			values.add(value);
		}
	}

	public Map<Integer, String> rawValues() {

		Map<Integer, String> result = CollectionUtils.map();
		for (BuildingAttributeTypeEnumValue value : values) {
			result.put(value.getOrder(), value.getValue());
		}

		return result;
	}

}
