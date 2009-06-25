package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * General description of external organizations, data bases, whatever our system needs to
 * be integrated with
 */
public class DataSourceDescription extends DomainObject {

	private String description;

	public DataSourceDescription() {
	}

	public DataSourceDescription(String description) {
		this.description = description;
	}

	public DataSourceDescription(Long id) {
		super(id);
	}

	public DataSourceDescription(Stub<DataSourceDescription> stub) {
		super(stub.getId());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("description", description).
				toString();
	}

}
