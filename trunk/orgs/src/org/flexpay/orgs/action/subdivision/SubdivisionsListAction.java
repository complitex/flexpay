package org.flexpay.orgs.action.subdivision;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class SubdivisionsListAction extends FPActionSupport {

	private Organization organization = new Organization();
	private List<Subdivision> subdivisions = CollectionUtils.list();

	private SubdivisionService subdivisionService;
	private OrganizationService organizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (organization.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
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
		Subdivision persistent = subdivisionService.read(stub(subdivision));
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

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
