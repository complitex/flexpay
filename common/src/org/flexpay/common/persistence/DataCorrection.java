package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.persistence.Stub.stub;

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

	@NotNull
	public Stub<DataSourceDescription> getDataSourceDescriptionStub() {
		return stub(dataSourceDescription);
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
				.append("dataSource", dataSourceDescription != null ? dataSourceDescription.getId() : null)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		DataCorrection that = (DataCorrection) o;

		if (objectType != that.objectType) return false;
		if (dataSourceDescription != null ? !dataSourceDescription.equals(that.dataSourceDescription) : that.dataSourceDescription != null)
			return false;
		if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
		if (internalObjectId != null ? !internalObjectId.equals(that.internalObjectId) : that.internalObjectId != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
		result = 31 * result + (internalObjectId != null ? internalObjectId.hashCode() : 0);
		result = 31 * result + objectType;
		result = 31 * result + (dataSourceDescription != null ? dataSourceDescription.hashCode() : 0);
		return result;
	}
}
