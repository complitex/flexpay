package org.flexpay.eirc.actions;

import java.util.List;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;

public class EircAccountListAction extends FPActionSupport {
	
	private EircAccountService eircAccountService;
	
	private List<EircAccount> eircAccountList;
	private Page<EircAccount> pager = new Page<EircAccount>();
	
	@NotNull
	public String doExecute() {
		
		eircAccountList = eircAccountService.findAll(pager);
		
		
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

	/**
	 * @param eircAccountService the eircAccountService to set
	 */
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	/**
	 * @return the pager
	 */
	public Page<EircAccount> getPager() {
		return pager;
	}

	/**
	 * @param pager the pager to set
	 */
	public void setPager(Page<EircAccount> pager) {
		this.pager = pager;
	}

	/**
	 * @return the eircAccountList
	 */
	public List<EircAccount> getEircAccountList() {
		return eircAccountList;
	}

}
