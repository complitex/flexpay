package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.hibernate.Hibernate;

public class IdentityTypeViewAction extends FPActionSupport {

	private Long id;
	private IdentityTypeService identityTypeService;
	private IdentityType identityType;

	@NotNull
	public String doExecute() throws Exception {
		identityType = identityTypeService.read(id);
        Hibernate.initialize(identityType.getTranslations());

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

	public void setId(Long id) {
		this.id = id;
	}

	public IdentityType getIdentityType() {
		return identityType;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}