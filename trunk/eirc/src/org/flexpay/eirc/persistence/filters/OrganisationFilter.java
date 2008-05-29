package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.Organisation;

import java.util.List;

public class OrganisationFilter extends PrimaryKeyFilter {

	private List<Organisation> organisations;

	public OrganisationFilter() {
		super(-1L);
	}

	public List<Organisation> getOrganisations() {
		return organisations;
	}

	public void setOrganisations(List<Organisation> organisations) {
		this.organisations = organisations;
	}

	public Organisation getSelected() {
		for (Organisation org : organisations) {
			if (org.getId().equals(getSelectedId())) {
				return org;
			}
		}

		return null;
	}
}
