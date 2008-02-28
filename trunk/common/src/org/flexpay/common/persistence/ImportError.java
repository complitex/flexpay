package org.flexpay.common.persistence;

/**
 * Persistent object to store import operations errors
 */
public class ImportError extends DomainObjectWithStatus {

	private DataSourceDescription sourceDescription;
	private String sourceObjectId;

	/**
	 * Value obtained from {@link org.flexpay.common.service.importexport.ClassToTypeRegistry}
	 */
	private int objectType;

	/**
	 * {@link org.flexpay.common.service.importexport.RawDataSource} instance name
	 */
	private String dataSourceBean;

	/**
	 * Constructs a new DomainObject.
	 */
	public ImportError() {
	}

	public ImportError(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'sourceDescription'.
	 *
	 * @return Value for property 'sourceDescription'.
	 */
	public DataSourceDescription getSourceDescription() {
		return sourceDescription;
	}

	/**
	 * Setter for property 'sourceDescription'.
	 *
	 * @param sourceDescription Value to set for property 'sourceDescription'.
	 */
	public void setSourceDescription(DataSourceDescription sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	/**
	 * Getter for property 'sourceObjectId'.
	 *
	 * @return Value for property 'sourceObjectId'.
	 */
	public String getSourceObjectId() {
		return sourceObjectId;
	}

	/**
	 * Setter for property 'sourceObjectId'.
	 *
	 * @param sourceObjectId Value to set for property 'sourceObjectId'.
	 */
	public void setSourceObjectId(String sourceObjectId) {
		this.sourceObjectId = sourceObjectId;
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

	/**
	 * Getter for property 'dataSourceBean'.
	 *
	 * @return Value for property 'dataSourceBean'.
	 */
	public String getDataSourceBean() {
		return dataSourceBean;
	}

	/**
	 * Setter for property 'dataSourceBean'.
	 *
	 * @param dataSourceBean Value to set for property 'dataSourceBean'.
	 */
	public void setDataSourceBean(String dataSourceBean) {
		this.dataSourceBean = dataSourceBean;
	}
}
