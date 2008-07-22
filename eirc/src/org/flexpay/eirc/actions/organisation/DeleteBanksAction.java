package org.flexpay.eirc.actions.organisation;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.BankService;

import java.util.HashSet;
import java.util.Set;

public class DeleteBanksAction extends FPActionSupport {

	private BankService bankService;

	private Set<Long> objectIds = new HashSet<Long>();

	public String doExecute() throws Exception {
		bankService.disable(objectIds);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}
}