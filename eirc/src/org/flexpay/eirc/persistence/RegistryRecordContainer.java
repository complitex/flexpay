package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RegistryRecordContainer extends DomainObject {

	private int order;
	private String data;
	private SpRegistryRecord record;

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

	public SpRegistryRecord getRecord() {
		return record;
	}

	public void setRecord(SpRegistryRecord record) {
		this.record = record;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("order", order)
				.append("data", data)
				.toString();
	}
}
