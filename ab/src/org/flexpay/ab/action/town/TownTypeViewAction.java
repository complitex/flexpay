package org.flexpay.ab.action.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class TownTypeViewAction extends FPActionSupport {

	private TownType townType = new TownType();

	private TownTypeService townTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (townType == null || townType.isNew()) {
			log.warn("Incorrect town type id");
			addActionError(getText("ab.error.town_type.incorrect_town_type_id"));
			return REDIRECT_ERROR;
		}

		Stub<TownType> stub = stub(townType);
		townType = townTypeService.readFull(stub);

		if (townType == null) {
			log.warn("Can't get town type with id {} from DB", stub.getId());
			addActionError(getText("ab.error.town_type.cant_get_town_type"));
			return REDIRECT_ERROR;
		} else if (townType.isNotActive()) {
			log.warn("Town type with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.town_type.cant_get_town_type"));
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

	public TownType getTownType() {
		return townType;
	}

	public void setTownType(TownType townType) {
		this.townType = townType;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
