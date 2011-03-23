package org.flexpay.orgs.persistence.filters;

import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;

import java.util.List;

public class ServiceOrganizationFilter
		extends OrganizationInstanceFilter<ServiceOrganizationDescription, ServiceOrganization> {

	private List<ServiceOrganization> organizations;

	public ServiceOrganizationFilter() {
		super(-1L);
	}

	public List<ServiceOrganization> getInstances() {
		return organizations;
	}

	public void setInstances(List<ServiceOrganization> organizations) {
		this.organizations = organizations;
	}

	public List<ServiceOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<ServiceOrganization> organizations) {
		this.organizations = organizations;
	}
}
