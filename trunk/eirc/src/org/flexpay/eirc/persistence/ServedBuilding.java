package org.flexpay.eirc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Building;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.flexpay.common.persistence.Stub.stub;

public class ServedBuilding extends BtiBuilding {

	private ServiceOrganization serviceOrganization;

	public ServedBuilding() {
	}

	public ServedBuilding(@NotNull Long id) {
		super(id);
	}

	public ServedBuilding(@NotNull Stub<? extends Building> stub) {
		super(stub);
	}

	public static ServedBuilding newInstance() {
		return new ServedBuilding();
	}

	public ServiceOrganization getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(ServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	@Nullable
	public Stub<ServiceOrganization> getServiceOrganizationStub() {
		return serviceOrganization == null ? null : stub(serviceOrganization);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("serviceOrganizationStub", getServiceOrganizationStub()).
				toString();
	}

}
