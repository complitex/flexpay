package org.flexpay.orgs.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.orgs.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class BankDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();

	private BankService bankService;

	@NotNull
	public String doExecute() throws Exception {
		bankService.disable(objectIds);

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
	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}

}
