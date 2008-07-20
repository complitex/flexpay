package org.flexpay.eirc.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.service.SPService;

import java.util.Collections;
import java.util.List;

public class ListServiceProvidersAction extends FPActionSupport {

	private SPService spService;

	private Page<ServiceProvider> pager = new Page<ServiceProvider>();
	private List<ServiceProvider> providers = Collections.emptyList();

	public String doExecute() throws Exception {

		providers = spService.listProviders(pager);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<ServiceProvider> getProviders() {
		return providers;
	}

	public Page<ServiceProvider> getPager() {
		return pager;
	}

	public void setPager(Page<ServiceProvider> pager) {
		this.pager = pager;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
