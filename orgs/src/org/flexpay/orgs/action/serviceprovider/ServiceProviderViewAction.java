package org.flexpay.orgs.action.serviceprovider;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceProviderViewAction extends FPActionSupport {

	private ServiceProvider provider = new ServiceProvider();

	private ServiceProviderService providerService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (provider.isNew()) {
			log.error(getText("common.error.invalid_id"));
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		provider = providerService.read(stub(provider));
		if (provider == null) {
			log.error(getText("common.error.object_not_selected"));
			addActionError(getText("common.error.object_not_selected"));
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

	public ServiceProvider getProvider() {
		return provider;
	}

	public void setProvider(ServiceProvider provider) {
		this.provider = provider;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

}
