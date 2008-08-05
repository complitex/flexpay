package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TownTypeListAction extends FPActionSupport {
	private TownTypeService townTypeService;
	private List<TownTypeTranslation> translationList;

	@NotNull
	public String doExecute() throws Exception {
		translationList = townTypeService.getTranslations(userPreferences.getLocale());

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
	 * Setter for property 'townTypeService'.
	 *
	 * @param townTypeService Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	public List<TownTypeTranslation> getTranslationList() {
		return translationList;
	}
}
