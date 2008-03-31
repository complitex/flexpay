package org.flexpay.eirc.persistence;

public class ServedBuilding extends org.flexpay.ab.persistence.Building {

	private Organisation serviceOrganisation;

	public Organisation getServiceOrganisation() {
		return serviceOrganisation;
	}

	public void setServiceOrganisation(Organisation serviceOrganisation) {
		this.serviceOrganisation = serviceOrganisation;
	}
}
