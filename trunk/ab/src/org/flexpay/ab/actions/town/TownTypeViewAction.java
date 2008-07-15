package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;

public class TownTypeViewAction extends FPActionSupport {
	private Long id;
	private TownTypeService townTypeService;
	private TownType townType;

	public String doExecute() throws Exception {
		townType = townTypeService.read(id);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TownType getTownType() {
		return townType;
	}

	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
