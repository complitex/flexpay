package org.flexpay.bti.persistence.apartment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

public class ApartmentAttributeTypeEnumValue extends DomainObject implements Comparable<ApartmentAttributeTypeEnumValue>{

	private ApartmentAttributeTypeEnum typeEnum;
	private String value;
	private int order;

	public ApartmentAttributeTypeEnumValue() {
	}

	public ApartmentAttributeTypeEnumValue(@NotNull Long id) {
		super(id);
	}

	public ApartmentAttributeTypeEnum getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(ApartmentAttributeTypeEnum typeEnum) {
		this.typeEnum = typeEnum;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int compareTo(ApartmentAttributeTypeEnumValue o) {
		return order - o.order;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("value", value).
				append("order", order).
				toString();
	}

}
