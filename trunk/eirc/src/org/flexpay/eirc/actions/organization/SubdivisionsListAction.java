package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.Subdivision;
import org.flexpay.eirc.service.OrganizationService;
import org.flexpay.eirc.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SubdivisionsListAction extends FPActionSupport {

	private SubdivisionService subdivisionService;
	private OrganizationService organizationService;

	private Organization organization = new Organization();
	private List<Subdivision> subdivisions = Collections.emptyList();

	@NotNull
	public String doExecute() throws Exception {

		if (organization.isNew()) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}
		subdivisions = subdivisionService.getOrganizationSubdivisions(stub(organization));

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
		return REDIRECT_SUCCESS;
	}

	public String getOrganizationName(@Nullable Organization org) throws FlexPayException {
		if (org == null || org.isNew()) {
			return "---";
		}
		Organization persistent = organizationService.readFull(stub(org));
		if (persistent == null) {
			log.warn("Invalid organisation requested {}", org);
			return "---";
		}
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
