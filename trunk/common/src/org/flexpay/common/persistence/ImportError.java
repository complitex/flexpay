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
	 * Error message localisation code
	 */
	private String errorId;

	public ImportError() {
	}

	public ImportError(Long id) {
		super(id);
	}

	public DataSourceDescription getSourceDescription() {
		return sourceDescription;
	}

	public void setSourceDescription(DataSourceDescription sourceDescription) {
		this.sourceDescription = sourceDescription;
	}

	public String getSourceObjectId() {
		return sourceObjectId;
	}

	public void setSourceObjectId(String sourceObjectId) {
		this.sourceObjectId = sourceObjectId;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public String getDataSourceBean() {
		return dataSourceBean;
	}

	public void setDataSourceBean(String dataSourceBean) {
		this.dataSourceBean = dataSourceBean;
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

}
