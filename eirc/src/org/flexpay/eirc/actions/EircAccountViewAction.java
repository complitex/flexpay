package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;

public class EircAccountViewAction extends FPActionSupport {
	
	private EircAccountService eircAccountService;
	
	private EircAccount eircAccount;
	
	public String execute() {
		
		eircAccount = eircAccountService.findWithPerson(eircAccount.getId());
		
		return "success";
	}

	/**
	 * @param eircAccountService the eircAccountService to set
	 */
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
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

}
