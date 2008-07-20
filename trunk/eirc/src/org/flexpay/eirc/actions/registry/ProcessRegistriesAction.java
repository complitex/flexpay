package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

public class ProcessRegistriesAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();

	private SpRegistryService registryService;
	private ServiceProviderFileProcessor providerFileProcessor;

	public String doExecute() throws Exception {

		if (objectIds.isEmpty()) {
			// just redirect, no registries to process
			return SUCCESS;
		}

		log.debug("About to execute ProcessRegistriesAction");

		Collection<SpRegistry> registries = registryService.findObjects(objectIds);
		providerFileProcessor.processRegistries(registries);
		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

	public void setProviderFileProcessor(ServiceProviderFileProcessor providerFileProcessor) {
		this.providerFileProcessor = providerFileProcessor;
	}
}
