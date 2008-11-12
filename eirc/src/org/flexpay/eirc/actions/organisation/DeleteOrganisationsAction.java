package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.eirc.service.OrganisationService;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DeleteOrganisationsAction extends FPActionSupport {

	private OrganisationService organisationService;

	private Set<Long> objectIds = set();

	@NotNull
	public String doExecute() throws Exception {
		organisationService.disable(objectIds);

		return REDIRECT_SUCCESS;
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
		return REDIRECT_SUCCESS;
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
