package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DataCorrection extends DomainObject {

	private String externalId;
	private Long internalObjectId;
	private int objectType;
	private DataSourceDescription dataSourceDescription;

	public DataCorrection() {
	}

	public DataCorrection(String externalId, Long internalObjectId, int objectType, DataSourceDescription dataSourceDescription) {
		this.externalId = externalId;
		this.internalObjectId = internalObjectId;
		this.objectType = objectType;
		this.dataSourceDescription = dataSourceDescription;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Long getInternalObjectId() {
		return internalObjectId;
	}

	public void setInternalObjectId(Long internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	public DataSourceDescription getDataSourceDescription() {
		return dataSourceDescription;
	}

	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("externalId", externalId)
				.append("internalObjectId", internalObjectId)
				.append("objectType", objectType)
				.append("dataSource", dataSourceDescription.getId())
				.toString();
	}

}
