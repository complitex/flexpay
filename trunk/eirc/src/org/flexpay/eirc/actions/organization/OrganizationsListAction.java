package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Collections;

public class OrganizationsListAction extends FPActionSupport {

	private OrganizationService organizationService;

	private Page<Organization> pager = new Page<Organization>();
	private List<Organization> organizations = Collections.emptyList();

	@NotNull
	public String doExecute() throws Exception {

		organizations = organizationService.listOrganizations(pager);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public Page<Organization> getPager() {
		return pager;
	}

	public void setPager(Page<Organization> pager) {
		this.pager = pager;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
