package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.persistence.Organisation;

import java.util.List;
import java.util.Collections;

public class ListOrganisationsAction extends FPActionSupport {

	private OrganisationService organisationService;

	private Page<Organisation> pager = new Page<Organisation>();
	private List<Organisation> organisations = Collections.emptyList();

	public String execute() throws Exception {

		organisations = organisationService.listOrganisations(pager);

		return SUCCESS;
	}

	public List<Organisation> getOrganisations() {
		return organisations;
	}

	public Page<Organisation> getPager() {
		return pager;
	}

	public void setPager(Page<Organisation> pager) {
		this.pager = pager;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}
}
