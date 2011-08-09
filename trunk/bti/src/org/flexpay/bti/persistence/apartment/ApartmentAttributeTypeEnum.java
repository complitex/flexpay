package org.flexpay.bti.persistence.apartment;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import static org.flexpay.common.util.CollectionUtils.set;

public class ApartmentAttributeTypeEnum extends ApartmentAttributeType {

	private Set<ApartmentAttributeTypeEnumValue> values = set();

	public ApartmentAttributeTypeEnum() {
	}

	public ApartmentAttributeTypeEnum(@NotNull Long id) {
		super(id);
	}

	public ApartmentAttributeTypeEnum(@NotNull Stub<ApartmentAttributeType> stub) {
		super(stub);
	}

	public Set<ApartmentAttributeTypeEnumValue> getValues() {
		return values;
	}

	public SortedSet<ApartmentAttributeTypeEnumValue> getSortedValues() {
		return CollectionUtils.treeSet(getValues());
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setValues(Set<ApartmentAttributeTypeEnumValue> values) {
		this.values = values;
	}

	@SuppressWarnings ({"CollectionsFieldAccessReplaceableByMethodCall"})
	public void addValue(ApartmentAttributeTypeEnumValue value) {
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
	public void validate(String value) throws FlexPayException {

		if (StringUtils.isBlank(value)) {
			throw new FlexPayException("No blank value allowed", "bti.error.apartment.attribute.enum.blank_value");
		}

		for (ApartmentAttributeTypeEnumValue enumValue : values) {
			if (value.equals(enumValue.getValue())) {
				return;
			}
		}

		throw new FlexPayException("No match", "bti.error.apartment.attribute.enum.no_match");
	}

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	public String getI18nTitle() {
		return "bti.apartment.attribute.type.enum";
	}

	/**
	 * Setup new enum values
	 *
	 * @param enumValues Order to value map to set up
	 */
	public void setRawValues(Map<Integer, String> enumValues) {

		Map<Integer, String> newValues = CollectionUtils.map(enumValues);
		Map<Integer, ApartmentAttributeTypeEnumValue> valuesMap = CollectionUtils.map();
		for (ApartmentAttributeTypeEnumValue oldValue : values) {
			valuesMap.put(oldValue.getOrder(), oldValue);
		}

		for (Map.Entry<Integer, ApartmentAttributeTypeEnumValue> entry : valuesMap.entrySet()) {
			if (newValues.containsKey(entry.getKey())) {
				// value with this order found, just update string value
				entry.getValue().setValue(newValues.get(entry.getKey()));
				newValues.remove(entry.getKey());
			} else {
				// no value with this order, remove enum value
				values.remove(entry.getValue());
			}
		}

		if (values == null) {
			values = set();
		}

		// now create all new values
		for (Map.Entry<Integer, String> entry : newValues.entrySet()) {
			ApartmentAttributeTypeEnumValue value = new ApartmentAttributeTypeEnumValue();
			value.setOrder(entry.getKey());
			value.setValue(entry.getValue());
			value.setTypeEnum(this);
			values.add(value);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("i18nTitle", getI18nTitle()).
				append("status", getStatus()).
				append("uniqueCode", getUniqueCode()).
				toString();
	}

}
