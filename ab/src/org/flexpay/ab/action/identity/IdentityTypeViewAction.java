package org.flexpay.ab.action.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class IdentityTypeViewAction extends FPActionSupport {

    private IdentityType identityType = new IdentityType();

	private IdentityTypeService identityTypeService;

    @NotNull
	@Override
    public String doExecute() throws Exception {

		if (identityType == null || identityType.isNew()) {
			log.warn("Incorrect identity type id");
			addActionError(getText("ab.error.identity_type.incorrect_identity_type_id"));
			return REDIRECT_ERROR;
		}

		Stub<IdentityType> stub = stub(identityType);
        identityType = identityTypeService.readFull(stub);

		if (identityType == null) {
			log.warn("Can't get identity type with id {} from DB", stub.getId());
			addActionError(getText("ab.error.identity_type.cant_get_identity_type"));
			return REDIRECT_ERROR;
		} else if (identityType.isNotActive()) {
			log.warn("Identity type with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.identity_type.cant_get_identity_type"));
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
