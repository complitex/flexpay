package org.flexpay.orgs.action.serviceprovider;

import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ServiceProvidersListAction extends FPActionWithPagerSupport<ServiceProvider> {

	private List<ServiceProvider> providers = CollectionUtils.list();

	private ServiceProviderService providerService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		providers = providerService.listInstances(getPager());

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
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

}
