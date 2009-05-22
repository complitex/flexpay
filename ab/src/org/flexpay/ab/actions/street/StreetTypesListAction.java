package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class StreetTypesListAction extends FPActionSupport {

	private List<StreetType> streetTypes;

	private StreetTypeService streetTypeService;

	@NotNull
	public String doExecute() throws Exception {
		streetTypes = streetTypeService.getEntities();

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

	public List<StreetType> getStreetTypes() {
		return streetTypes;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
