package org.flexpay.orgs.actions.subdivision;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

public class SubdivisionsListPageAction extends FPActionSupport {

	private Organization organization = new Organization();

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (organization.isNew()) {
			log.error(getText("error.invalid_id"));
			addActionError(getText("error.invalid_id"));
			return SUCCESS;
		}

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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
