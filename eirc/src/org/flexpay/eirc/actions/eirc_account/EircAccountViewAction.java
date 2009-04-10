package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class EircAccountViewAction extends FPActionSupport {
	
	private EircAccount eircAccount;
	private SPService spService;

	private EircAccountService eircAccountService;

	@NotNull
	public String doExecute() {
		
		eircAccount = eircAccountService.readFull(stub(eircAccount));
		
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

	public String getServiceDescription(@NotNull Service service) throws Exception {
		Service persistent = spService.read(stub(service));
		return persistent.format(getLocale());
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
