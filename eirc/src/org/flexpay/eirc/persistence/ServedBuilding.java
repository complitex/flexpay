package org.flexpay.eirc.persistence;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;

public class ServedBuilding extends Building {

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
