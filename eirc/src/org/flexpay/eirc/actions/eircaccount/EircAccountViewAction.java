package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class EircAccountViewAction extends FPActionSupport {
	
	private EircAccount eircAccount = new EircAccount();
	private SPService spService;

	private EircAccountService eircAccountService;

	@NotNull
	@Override
	public String doExecute() {
		
		if (eircAccount.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		eircAccount = eircAccountService.readFull(stub(eircAccount));
		
		if (eircAccount == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

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
		return REDIRECT_ERROR;
	}

	public String getServiceDescription(@NotNull Service service) throws Exception {
		Service persistent = spService.readFull(stub(service));
		return persistent == null ? "" : persistent.format(getLocale());
	}

	public EircAccount getEircAccount() {
		return eircAccount;
	}

	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	@Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

}
