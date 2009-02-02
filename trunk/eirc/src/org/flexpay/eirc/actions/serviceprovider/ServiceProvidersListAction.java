package org.flexpay.eirc.actions.serviceprovider;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class ServiceProvidersListAction extends FPActionWithPagerSupport<ServiceProvider> {

	private List<ServiceProvider> providers = Collections.emptyList();

	private SPService spService;

	@NotNull
	public String doExecute() throws Exception {

		providers = spService.listProviders(getPager());

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
		return SUCCESS;
	}

	public List<ServiceProvider> getProviders() {
		return providers;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

}
