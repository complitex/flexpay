package org.flexpay.eirc.persistence.consumer;

import org.flexpay.common.exception.FlexPayException;

public class ConsumerAttributeTypeSimple extends ConsumerAttributeTypeBase {

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if validation fails
	 */
	@Override
	public void validate(ConsumerAttribute value) throws FlexPayException {
		// every value is valid
	}

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	@Override
	public String getI18nTitle() {
		return "eirc.consumer.attribute.type.simple";
	}
}
