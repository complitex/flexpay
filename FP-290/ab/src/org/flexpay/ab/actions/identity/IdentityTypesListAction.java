package org.flexpay.ab.actions.identity;

import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IdentityTypesListAction extends FPActionSupport {

	private IdentityTypeService identityTypeService;
	private List<IdentityTypeTranslation> translationList;

	@NotNull
	public String doExecute() throws Exception {
		translationList = identityTypeService.getTranslations(userPreferences.getLocale());

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

	/**
	 * Setter for property 'identityTypeService'.
	 *
	 * @param identityTypeService Value to set for property 'identityTypeService'.
	 */
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

	public List<IdentityTypeTranslation> getTranslationList() {
		return translationList;
	}

}
