package org.flexpay.eirc.persistence;

import org.flexpay.ab.persistence.Building;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;

public class ServedBuilding extends BtiBuilding {

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

	private ServiceOrganization serviceOrganization;

	public ServiceOrganization getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(ServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public Stub<ServiceOrganization> getServiceOrganizationStub() {
		return stub(serviceOrganization);
	}
}
