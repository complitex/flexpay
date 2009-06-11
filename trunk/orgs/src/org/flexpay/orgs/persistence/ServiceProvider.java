package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("name", getName()).
				append("status", getStatus()).
				toString();
	}

}
