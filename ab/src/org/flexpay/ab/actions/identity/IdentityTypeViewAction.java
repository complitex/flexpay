package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class IdentityTypeViewAction extends FPActionSupport {

    private IdentityType identityType = new IdentityType();

	private IdentityTypeService identityTypeService;

    @NotNull
	@Override
    public String doExecute() throws Exception {

		if (identityType.isNew()) {
			log.error(getText("error.invalid_id"));
			return REDIRECT_ERROR;
		}

        identityType = identityTypeService.read(stub(identityType));

		if (identityType == null) {
			log.error(getText("common.object_not_selected"));
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
