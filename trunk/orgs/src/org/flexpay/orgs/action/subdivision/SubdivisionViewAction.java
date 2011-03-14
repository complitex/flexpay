package org.flexpay.orgs.action.subdivision;

import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class SubdivisionViewAction extends FPActionSupport {

	private Subdivision subdivision = new Subdivision();

	private SubdivisionService subdivisionService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (subdivision.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}
		subdivision = subdivisionService.read(stub(subdivision));

		if (subdivision == null) {
			log.error(getText("common.object_not_selected"));
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public Subdivision getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(Subdivision subdivision) {
		this.subdivision = subdivision;
	}

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}

}
