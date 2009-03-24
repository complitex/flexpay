package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Collections;

/**
 * List quittances of eirc aсcount for the last period
 */
public class QuittancesListAction extends FPActionWithPagerSupport<Quittance> {

	private EircAccount account = new EircAccount();
	private List<Quittance> quittances = Collections.emptyList();

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
	protected String doExecute() throws Exception {

		if (account.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		account = accountService.readFull(stub(account));
		if (account == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_ERROR;
		}

		quittances = quittanceService.getLatestAccountQuittances(stub(account));

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
		return REDIRECT_ERROR;
	}

	public EircAccount getAccount() {
		return account;
	}

	public void setAccount(EircAccount account) {
		this.account = account;
	}

	public List<Quittance> getQuittances() {
		return quittances;
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
