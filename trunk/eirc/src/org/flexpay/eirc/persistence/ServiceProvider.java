package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DataSourceDescription;

public class ServiceProvider extends DomainObject {

	private Long providerNumber;
	private String description;
	private Organisation organisation;
	private DataSourceDescription dataSourceDescription;

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceProvider() {
	}

	public ServiceProvider(Long id) {
		super(id);
	}

	public Long getProviderNumber() {
		return providerNumber;
	}

	public void setProviderNumber(Long providerNumber) {
		this.providerNumber = providerNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
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
}
