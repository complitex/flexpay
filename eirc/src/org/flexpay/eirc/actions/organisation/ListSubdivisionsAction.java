package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.Subdivision;
import org.flexpay.eirc.service.SubdivisionService;
import org.flexpay.eirc.service.OrganisationService;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ListSubdivisionsAction extends FPActionSupport {

	private SubdivisionService subdivisionService;
	private OrganisationService organisationService;

	private Organisation organisation = new Organisation();
	private List<Subdivision> subdivisions = Collections.emptyList();

	public String doExecute() throws Exception {

		if (organisation.isNew()) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}
		subdivisions = subdivisionService.getOrganisationSubdivisions(stub(organisation));

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
		return REDIRECT_SUCCESS;
	}

	public String getOrganisationName(@Nullable Organisation org) throws FlexPayException {
		if (org == null) {
			return "---";
		}
		Organisation persistent = organisationService.read(org);
		return getTranslation(persistent.getNames()).getName();
	}

	public String getSubdivisionName(@Nullable Subdivision subdivision) throws FlexPayException {
		if (subdivision == null) {
			return "---";
		}
		Subdivision persistent = subdivisionService.read(subdivision);
		if (persistent == null) {
			log.error("Invalid Subdivision requested: #" + subdivision.getId());
			throw new RuntimeException("");
		}
		return getTranslation(persistent.getNames()).getName();
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public List<Subdivision> getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(List<Subdivision> subdivisions) {
		this.subdivisions = subdivisions;
	}

	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}
}
