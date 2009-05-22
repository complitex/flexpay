package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class IdentityTypeViewAction extends FPActionSupport {

    private Long id;
    private IdentityType identityType;

	private IdentityTypeService identityTypeService;

    @NotNull
    public String doExecute() throws Exception {
        identityType = identityTypeService.read(new Stub<IdentityType>(id));

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
        return SUCCESS;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

	@Required
    public void setIdentityTypeService(IdentityTypeService identityTypeService) {
        this.identityTypeService = identityTypeService;
    }

}
