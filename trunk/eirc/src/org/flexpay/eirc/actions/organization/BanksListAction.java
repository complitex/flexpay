package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class BanksListAction extends FPActionWithPagerSupport<Bank> {

	private List<Bank> banks = Collections.emptyList();

	private BankService bankService;

	@NotNull
	public String doExecute() throws Exception {

		banks = bankService.listInstances(getPager());

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

	public List<Bank> getBanks() {
		return banks;
	}

	@Required
	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}

}
