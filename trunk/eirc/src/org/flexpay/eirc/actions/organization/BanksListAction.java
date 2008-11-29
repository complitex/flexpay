package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.Bank;
import org.flexpay.eirc.service.BankService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BanksListAction extends FPActionSupport {

	private BankService bankService;

	private Page<Bank> pager = new Page<Bank>();
	private List<Bank> banks = Collections.emptyList();

	@NotNull
	public String doExecute() throws Exception {

		banks = bankService.listBanks(pager);

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

	public Page<Bank> getPager() {
		return pager;
	}

	public void setPager(Page<Bank> pager) {
		this.pager = pager;
	}

	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}
}
