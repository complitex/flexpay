package org.flexpay.eirc.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class DeleteServiceProvidersAction extends FPActionSupport {

	private SPService spService;

	private Set<Long> objectIds = new HashSet<Long>();

	@NotNull
	public String doExecute() throws Exception {
		spService.disable(objectIds);

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

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}