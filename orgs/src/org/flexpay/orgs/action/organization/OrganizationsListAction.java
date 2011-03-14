package org.flexpay.orgs.action.organization;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class OrganizationsListAction extends FPActionWithPagerSupport<Organization> {

	private List<Organization> organizations = CollectionUtils.list();

	private OrganizationService organizationService;

	@NotNull
	@Override
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
	@Override
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
