package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class TownTypesListAction extends FPActionSupport {

	private List<TownType> townTypes;

	private TownTypeService townTypeService;

	@NotNull
	public String doExecute() throws Exception {
		townTypes = townTypeService.getEntities();

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

	public List<TownType> getTownTypes() {
		return townTypes;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
