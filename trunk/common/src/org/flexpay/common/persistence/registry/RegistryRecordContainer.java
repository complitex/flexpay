package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

public class RegistryRecordContainer extends DomainObject {

	private int order;
	private String data;
	private RegistryRecord record;

	public RegistryRecordContainer() {
	}

	public RegistryRecordContainer(String data) {
		this.data = data;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("RegistryRecordContainer {").
				append("id", getId()).
				append("order", order).
				append("data", data).
				append("record.id", record.getId()).
				append("}").toString();
	}

}
