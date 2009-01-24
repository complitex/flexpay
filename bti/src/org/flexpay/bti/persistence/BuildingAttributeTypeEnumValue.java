package org.flexpay.bti.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

public class BuildingAttributeTypeEnumValue extends DomainObject {

	private BuildingAttributeTypeEnum typeEnum;
	private String value;
	private int order;

	/**
	 * Constructs a new DomainObject.
	 */
	public BuildingAttributeTypeEnumValue() {
	}

	public BuildingAttributeTypeEnumValue(@NotNull Long id) {
		super(id);
	}

	public BuildingAttributeTypeEnum getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(BuildingAttributeTypeEnum typeEnum) {
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

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("typeEnum", typeEnum).
				append("value", value).
				append("order", order).
				toString();
	}
}