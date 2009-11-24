package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class StreetTypeViewAction extends FPActionSupport {

	private StreetType streetType = new StreetType();

	private StreetTypeService streetTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (streetType == null || streetType.isNew()) {
			log.debug("Incorrect street type id");
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		Stub<StreetType> stub = stub(streetType);
		streetType = streetTypeService.readFull(stub);

		if (streetType == null) {
			log.debug("Can't get street type with id {} from DB", stub.getId());
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		} else if (streetType.isNotActive()) {
			log.debug("Street type with id {} is disabled", stub.getId());
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
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
		return REDIRECT_ERROR;
	}

	public StreetType getStreetType() {
		return streetType;
	}

	public void setStreetType(StreetType streetType) {
		this.streetType = streetType;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

}
