package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;

public class ServedBuilding extends org.flexpay.ab.persistence.Building {

	private ServiceOrganisation serviceOrganisation;

	public ServiceOrganisation getServiceOrganisation() {
		return serviceOrganisation;
	}

	public void setServiceOrganisation(ServiceOrganisation serviceOrganisation) {
		this.serviceOrganisation = serviceOrganisation;
	}

	public Stub<ServiceOrganisation> getServiceOrganisationStub() {
		return stub(serviceOrganisation);
	}
}
