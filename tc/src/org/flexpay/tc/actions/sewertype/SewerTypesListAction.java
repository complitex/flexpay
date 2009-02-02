package org.flexpay.tc.actions.sewertype;

import org.flexpay.bti.persistence.SewerType;
import org.flexpay.bti.service.SewerTypesService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class SewerTypesListAction extends FPActionWithPagerSupport<SewerType> {

	private List<SewerType> sewerTypes = Collections.emptyList();

	private SewerTypesService sewerTypesService;

	@NotNull
	public String doExecute() {

		sewerTypes = sewerTypesService.listSewerTypes(getPager());

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

	public List<SewerType> getSewerTypes() {
		return sewerTypes;
	}

	@Required
	public void setSewerTypesService(SewerTypesService sewerTypesService) {
		this.sewerTypesService = sewerTypesService;
	}

}
