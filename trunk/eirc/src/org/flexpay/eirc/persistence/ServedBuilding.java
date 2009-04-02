package org.flexpay.eirc.persistence;

import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.orgs.persistence.ServiceOrganization;

public class ServedBuilding extends BtiBuilding {

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
