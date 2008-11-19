package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StreetTypesListAction extends FPActionSupport {

	private StreetTypeService streetTypeService;
	private List<StreetTypeTranslation> translationList;

	@NotNull
	public String doExecute() throws Exception {
		translationList = streetTypeService.getTranslations(userPreferences.getLocale());

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
	 * Setter for property 'streetTypeService'.
	 *
	 * @param streetTypeService Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public List<StreetTypeTranslation> getTranslationList() {
		return translationList;
	}
}
