package org.flexpay.orgs.action.bank;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.service.BankService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BanksListAction extends FPActionWithPagerSupport<Bank> {

	private List<Bank> banks = CollectionUtils.list();

	private BankService bankService;

	@NotNull
	@Override
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
	@Override
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
