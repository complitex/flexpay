package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

public class ServiceProvider extends OrganizationInstance<ServiceProviderDescription, ServiceProvider> {

	private DataSourceDescription dataSourceDescription;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
	public Stub<DataSourceDescription> getDataSourceDescriptionStub() {
		return stub(dataSourceDescription);
	}

	public void setDataSourceDescription(DataSourceDescription dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public String getDefaultDescription() {
		ServiceProviderDescription desc = TranslationUtil.getTranslation(getDescriptions());
		return desc != null ? desc.getName() : "";
	}
}
