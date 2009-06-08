package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.orgs.persistence.ServiceOrganization;

import java.util.List;

public class ServiceOrganizationFilter extends PrimaryKeyFilter<ServiceOrganization> {

	private List<ServiceOrganization> organizations;

	public ServiceOrganizationFilter() {
		super(-1L);
	}

	public List<ServiceOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<ServiceOrganization> organizations) {
		this.organizations = organizations;
	}

}