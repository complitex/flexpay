package org.flexpay.eirc.persistence.consumer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.TemporalValueObject;
import org.flexpay.eirc.persistence.Consumer;

public class ConsumerAttribute extends TemporalValueObject implements Comparable<ConsumerAttribute> {

	private ConsumerAttributeTypeBase type;
	private Consumer consumer;
	private int isTemporal = 0;

	public ConsumerAttributeTypeBase getType() {
		return type;
	}

	public void setType(ConsumerAttributeTypeBase type) {
		this.type = type;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	public int getTemporal() {
		return isTemporal;
	}

	public void setTemporal(int temporal) {
		isTemporal = temporal;
	}

	@Override
	public int compareTo(ConsumerAttribute o) {
		return getBegin().compareTo(o.getBegin());
	}

	@Override
	protected ToStringBuilder buildToString(ToStringBuilder builder) {
		return super.buildToString(builder)
				.append("type-id", type == null ? null : type.getId())
				.append("consumer-id", consumer == null ? null : consumer.getId());
	}

	public ConsumerAttribute copy() {

		ConsumerAttribute attribute = new ConsumerAttribute();
		attribute.setConsumer(consumer);
		attribute.setType(type);
		attribute.setBegin(getBegin());
		attribute.setEnd(getEnd());
		attribute.setTemporal(getTemporal());
		if (isBool()) {
			attribute.setBoolValue(isBoolValue());
		} else if (isDate()) {
			attribute.setDateValue(getDateValue());
		} else if (isDecimal()) {
			attribute.setDecimalValue(getDecimalValue());
		} else if (isDouble()) {
			attribute.setDoubleValue(getDoubleValue());
		} else if (isInt()) {
			attribute.setIntValue(getIntValue());
		} else if (isLong()) {
			attribute.setLongValue(getLongValue());
		} else if (isString()) {
			attribute.setStringValue(getStringValue());
		}

		return attribute;
	}

}
