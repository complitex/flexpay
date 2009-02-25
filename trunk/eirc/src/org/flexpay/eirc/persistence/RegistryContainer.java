package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

public class RegistryContainer extends DomainObject {

	private int order;
	private String data;
	private SpRegistry registry;

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

	public SpRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("order", order)
				.append("data", data)
				.toString();
	}
}
