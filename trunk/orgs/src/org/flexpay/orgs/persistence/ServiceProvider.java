package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;

public class ServiceProvider extends OrganizationInstance<ServiceProviderDescription, ServiceProvider> {

	private DataSourceDescription dataSourceDescription;

	public ServiceProvider() {
	}

	public ServiceProvider(Long id) {
		super(id);
	}

	public ServiceProvider(Stub<ServiceProvider> stub) {
		super(stub.getId());
	}

	public DataSourceDescription getDataSourceDescription() {
		return dataSourceDescription;
	}

	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public String getDefaultDescription() {
		ServiceProviderDescription desc = TranslationUtil.getTranslation(getDescriptions());
		return desc != null ? desc.getName() : "";
	}
}
