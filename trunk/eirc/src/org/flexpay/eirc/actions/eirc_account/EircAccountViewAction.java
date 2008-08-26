package org.flexpay.eirc.actions.eirc_account;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;

public class EircAccountViewAction extends FPActionSupport {
	
	private EircAccountService eircAccountService;
	
	private EircAccount eircAccount;
	private SPService spService;

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

	/**
	 * @return the eircAccount
	 */
	public EircAccount getEircAccount() {
		return eircAccount;
	}

	/**
	 * @param eircAccount the eircAccount to set
	 */
	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
