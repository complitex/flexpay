package org.flexpay.orgs.action.organization;

import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class OrganizationDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private OrganizationService organizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		organizationService.disable(objectIds);

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

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
