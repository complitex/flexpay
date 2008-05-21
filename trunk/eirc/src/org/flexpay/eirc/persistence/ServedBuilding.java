package org.flexpay.eirc.persistence;

public class ServedBuilding extends org.flexpay.ab.persistence.Building {

	private ServiceOrganisation serviceOrganisation;

	public ServiceOrganisation getServiceOrganisation() {
		return serviceOrganisation;
	}

	public void setServiceOrganisation(ServiceOrganisation serviceOrganisation) {
		this.serviceOrganisation = serviceOrganisation;
	}
}
