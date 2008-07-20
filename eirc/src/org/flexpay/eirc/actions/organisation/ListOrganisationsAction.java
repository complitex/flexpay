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

	public String doExecute() throws Exception {

		organisations = organisationService.listOrganisations(pager);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
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
