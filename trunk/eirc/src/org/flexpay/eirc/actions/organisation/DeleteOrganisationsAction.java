package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.service.imp.OrganisationServiceImpl;

import java.util.HashSet;
import java.util.Set;

public class DeleteOrganisationsAction extends FPActionSupport {

	private OrganisationService organisationService;

	private Set<Long> objectIds = new HashSet<Long>();

	public String execute() throws Exception {
		organisationService.disable(objectIds);

		return SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}
}
