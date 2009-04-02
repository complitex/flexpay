package org.flexpay.orgs.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.orgs.persistence.Organization;

import java.util.List;

public class OrganizationFilter extends PrimaryKeyFilter<Organization> {

	private List<Organization> organizations;

	public OrganizationFilter() {
		super(-1L);
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	public Organization getSelected() {
		for (Organization org : organizations) {
			if (org.getId().equals(getSelectedId())) {
				return org;
			}
		}

		return null;
	}
}
