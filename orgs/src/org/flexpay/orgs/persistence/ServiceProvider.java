package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.util.TranslationUtil;

public class ServiceProvider extends OrganizationInstance<ServiceProviderDescription, ServiceProvider> {

	private DataSourceDescription dataSourceDescription;

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceProvider() {
	}

	public ServiceProvider(Long id) {
		super(id);
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

	public String getDefaultDescription() {
		ServiceProviderDescription desc = TranslationUtil.getTranslation(getDescriptions());
		return desc != null ? desc.getName() : "";
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("ServiceProvider {").
				append("id", getId()).
				append("name", getName()).
				append("organization", getOrganization()).
				append("dataSourceDescription", dataSourceDescription).
				append("}").toString();
	}

}
