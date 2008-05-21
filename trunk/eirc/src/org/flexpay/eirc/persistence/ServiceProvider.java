package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.DataSourceDescription;

import java.util.Set;
import java.util.Collections;

public class ServiceProvider extends DomainObject {

	private Long providerNumber;
	private Set<ServiceProviderDescription> descriptions = Collections.emptySet();
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

	public Set<ServiceProviderDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ServiceProviderDescription> descriptions) {
		this.descriptions = descriptions;
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
