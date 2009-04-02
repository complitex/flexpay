package org.flexpay.orgs.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class ServiceProviderDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();

	private ServiceProviderService providerService;

	@NotNull
	public String doExecute() throws Exception {

		providerService.disable(objectIds);

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

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}
}
