package org.flexpay.orgs.actions.serviceprovider;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class ServiceProvidersListAction extends FPActionWithPagerSupport<ServiceProvider> {

	private List<ServiceProvider> providers = Collections.emptyList();

	private ServiceProviderService providerService;

	@NotNull
	public String doExecute() throws Exception {

		providers = providerService.listProviders(getPager());

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

	public List<ServiceProvider> getProviders() {
		return providers;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

}
