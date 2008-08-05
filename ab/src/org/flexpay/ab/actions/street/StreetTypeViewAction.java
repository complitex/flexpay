package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class StreetTypeViewAction extends FPActionSupport {

	private Long id;
	private StreetTypeService streetTypeService;
	private StreetType streetType;

	@NotNull
	public String doExecute() throws Exception {
		streetType = streetTypeService.read(id);

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

	public StreetType getStreetType() {
		return streetType;
	}

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
