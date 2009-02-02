package org.flexpay.tc.actions.sewertype;

import org.flexpay.bti.service.SewerTypesService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class SewerTypeDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();

	private SewerTypesService sewerTypesService;

	@NotNull
	public String doExecute() {

		sewerTypesService.disable(objectIds);

		return REDIRECT_SUCCESS;
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
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setSewerTypesService(SewerTypesService sewerTypesService) {
		this.sewerTypesService = sewerTypesService;
	}

}
