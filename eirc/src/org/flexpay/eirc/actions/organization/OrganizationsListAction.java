package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class OrganizationsListAction extends FPActionWithPagerSupport<Organization> {

	private List<Organization> organizations = Collections.emptyList();

	private OrganizationService organizationService;

	@NotNull
	public String doExecute() throws Exception {

		organizations = organizationService.listOrganizations(getPager());

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

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
