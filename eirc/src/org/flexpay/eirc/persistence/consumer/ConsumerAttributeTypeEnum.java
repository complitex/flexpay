package org.flexpay.eirc.persistence.consumer;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class ConsumerAttributeTypeEnum extends ConsumerAttributeTypeBase {

	private List<ConsumerAttributeTypeEnumValue> values = Collections.emptyList();

	public List<ConsumerAttributeTypeEnumValue> getValues() {
		return values;
	}

	public void setValues(List<ConsumerAttributeTypeEnumValue> values) {
		this.values = values;
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if validation fails
	 */
	@Override
	public void validate(ConsumerAttribute value) throws FlexPayException {

		if (value == null) {
			throw new FlexPayException("Blank value", "eirc.error.consumer.attribute.enum.blank_value");
		}

		for (ConsumerAttributeTypeEnumValue enumValue : values) {
			if (enumValue.sameValue(value)) {
				return;
			}
		}

		throw new FlexPayException("No match", "eirc.error.consumer.attribute.enum.no_match");
	}

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	@Override
	public String getI18nTitle() {
		return "eirc.consumer.attribute.type.enum";
	}

	public void addValue(ConsumerAttributeTypeEnumValue enumValue) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (values == Collections.EMPTY_LIST) {
			values = CollectionUtils.list();
		}
		enumValue.setOrder(values.size());
		enumValue.setTypeEnum(this);
		values.add(enumValue);
	}
}
