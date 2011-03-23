package org.flexpay.eirc.persistence.consumer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.ValueObject;

public class ConsumerAttributeTypeEnumValue extends ValueObject {

	private ConsumerAttributeTypeEnum typeEnum;
	private int order;

	@Override
	protected ToStringBuilder buildToString(ToStringBuilder builder) {
		return builder
				.append("order", order);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public ConsumerAttributeTypeEnum getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(ConsumerAttributeTypeEnum typeEnum) {
		this.typeEnum = typeEnum;
	}
}
