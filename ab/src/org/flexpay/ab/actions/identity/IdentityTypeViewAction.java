package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class IdentityTypeViewAction extends FPActionSupport {

    private IdentityType identityType = new IdentityType();

	private IdentityTypeService identityTypeService;

    @NotNull
	@Override
    public String doExecute() throws Exception {

		if (identityType == null || identityType.isNew()) {
			log.debug("Incorrect identity type id");
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		Stub<IdentityType> stub = stub(identityType);
        identityType = identityTypeService.readFull(stub);

		if (identityType == null) {
			log.debug("Can't get identity type with id {} from DB", stub.getId());
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		} else if (identityType.isNotActive()) {
			log.debug("Identity type with id {} is disabled", stub.getId());
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

	public IdentityType getIdentityType() {
		return identityType;
	}

	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}

	@Required
    public void setIdentityTypeService(IdentityTypeService identityTypeService) {
        this.identityTypeService = identityTypeService;
    }

}
