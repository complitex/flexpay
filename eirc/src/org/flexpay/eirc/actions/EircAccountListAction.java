package org.flexpay.eirc.actions;

import java.util.List;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;

public class EircAccountListAction extends FPActionSupport {
	
	private EircAccountService eircAccountService;
	
	private List<EircAccount> eircAccountList;
	private Page pager = new Page();
	
	public String execute() {
		
		eircAccountList = eircAccountService.findAll(pager);
		
		
		return "success";
	}

	/**
	 * @param eircAccountService the eircAccountService to set
	 */
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	/**
	 * @return the pager
	 */
	public Page getPager() {
		return pager;
	}

	/**
	 * @param pager the pager to set
	 */
	public void setPager(Page pager) {
		this.pager = pager;
	}

	/**
	 * @return the eircAccountList
	 */
	public List<EircAccount> getEircAccountList() {
		return eircAccountList;
	}

}
