package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DataCorrection extends DomainObject {

	private String externalId;
	private Long internalObjectId;
	private int objectType;
	private DataSourceDescription dataSourceDescription;

	/**
	 * Constructs a new DataCorrection.
	 */
	public DataCorrection() {
	}

	/**
	 * Getter for property 'externalId'.
	 *
	 * @return Value for property 'externalId'.
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Setter for property 'externalId'.
	 *
	 * @param externalId Value to set for property 'externalId'.
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Getter for property 'internalObjectId'.
	 *
	 * @return Value for property 'internalObjectId'.
	 */
	public Long getInternalObjectId() {
		return internalObjectId;
	}

	/**
	 * Setter for property 'internalObjectId'.
	 *
	 * @param internalObjectId Value to set for property 'internalObjectId'.
	 */
	public void setInternalObjectId(Long internalObjectId) {
		this.internalObjectId = internalObjectId;
	}

	/**
	 * Getter for property 'dataSourceDescription'.
	 *
	 * @return Value for property 'dataSourceDescription'.
	 */
	public DataSourceDescription getDataSourceDescription() {
		return dataSourceDescription;
	}

	/**
	 * Setter for property 'dataSourceDescription'.
	 *
	 * @param dataSourceDescription Value to set for property 'dataSourceDescription'.
	 */
	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	/**
	 * Getter for property 'objectType'.
	 *
	 * @return Value for property 'objectType'.
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * Setter for property 'objectType'.
	 *
	 * @param objectType Value to set for property 'objectType'.
	 */
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
