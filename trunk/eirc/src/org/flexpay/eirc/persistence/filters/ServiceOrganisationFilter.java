package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.ServiceOrganisation;

import java.util.List;

public class ServiceOrganisationFilter extends PrimaryKeyFilter<ServiceOrganisation> {

	private List<ServiceOrganisation> organisations;

	public ServiceOrganisationFilter() {
		super(-1L);
	}

	public List<ServiceOrganisation> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<ServiceOrganisation> organisations) {
		this.organisations = organisations;
	}
}