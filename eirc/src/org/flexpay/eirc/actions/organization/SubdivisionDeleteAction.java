package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SubdivisionDeleteAction extends FPActionSupport {

	private SubdivisionService subdivisionService;

	private Organization organization = new Organization();
	private Set<Long> objectIds = set();

	@NotNull
	public String doExecute() throws Exception {

		if (organization.isNew()) {
			log.warn("No organization specified");
			return REDIRECT_ERROR;
		}

		subdivisionService.disable(objectIds);

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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}
}
