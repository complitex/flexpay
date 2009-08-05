package org.flexpay.ab.actions.town;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

//TODO: This class has a out-of-date structure. Must be remake
public class TownTypeViewAction extends FPActionSupport {

	private Long id;
	private TownType townType;

	private TownTypeService townTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {
		townType = townTypeService.read(new Stub<TownType>(id));

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

	public TownType getTownType() {
		return townType;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

}
