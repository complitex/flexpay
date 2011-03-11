package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class QuittanceSearchByAccountAction extends FPActionSupport {

	private String accountNumber;
	private Quittance quittance;

	private EircAccountService accountService;
	private QuittanceService quittanceService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
    @Override
	protected String doExecute() throws Exception {

		if (StringUtils.isBlank(accountNumber)) {
			return INPUT;
		}

		EircAccount account = accountService.findAccount(accountNumber);
		if (account == null) {
			addActionError(getText("eirc.error.account_not_found"));
			return INPUT;
		}

		List<Quittance> quittances = quittanceService.getLatestAccountQuittances(account);
		if (quittances.isEmpty()) {
			addActionError(getText("eirc.error.quittance.no_for_account"));
			return INPUT;
		}

		quittance = quittances.get(0);
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
		return INPUT;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Quittance getQuittance() {
		return quittance;
	}

	@Required
	public void setAccountService(EircAccountService accountService) {
		this.accountService = accountService;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}
