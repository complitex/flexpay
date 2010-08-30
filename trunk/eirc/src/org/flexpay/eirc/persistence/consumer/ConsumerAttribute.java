package org.flexpay.eirc.persistence.consumer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.TemporalValueObject;
import org.flexpay.eirc.persistence.Consumer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Date;

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

	public void setValue(@NotNull Object value) {
		if (notEmpty()) {
			updateValue(value);
			return;
		}
		if (value instanceof Integer) {
			setIntValue((Integer)value);
		} else if (value instanceof Double) {
			setDoubleValue((Double)value);
		} else if (value instanceof Boolean) {
			setBoolValue((Boolean)value);
		} else if (value instanceof BigDecimal) {
			setDecimalValue((BigDecimal)value);
		} else if (value instanceof String) {
			setStringValue((String)value);
		} else if (value instanceof Date) {
			setDateValue((Date)value);
		} else {
			throw new IllegalStateException("unsupported type: " + value.getClass().getName());
		}
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
