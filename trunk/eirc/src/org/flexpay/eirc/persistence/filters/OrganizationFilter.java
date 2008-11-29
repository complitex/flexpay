package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.Organization;

import java.util.List;

public class OrganizationFilter extends PrimaryKeyFilter {

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
