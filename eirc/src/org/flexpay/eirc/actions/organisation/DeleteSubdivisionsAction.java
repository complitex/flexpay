package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.eirc.service.SubdivisionService;
import org.flexpay.eirc.persistence.Organisation;

import java.util.Set;

public class DeleteSubdivisionsAction extends FPActionSupport {

	private SubdivisionService subdivisionService;

	private Organisation organisation = new Organisation();
	private Set<Long> objectIds = set();

	public String doExecute() throws Exception {

		if (organisation.isNew()) {
			log.warn("No organisation specified");
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
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}
}
