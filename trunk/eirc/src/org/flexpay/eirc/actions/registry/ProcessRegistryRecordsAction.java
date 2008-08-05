package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ProcessRegistryRecordsAction extends FPActionSupport {

	private Set<Long> objectIds = new HashSet<Long>();
	private SpRegistry registry = new SpRegistry();

	private SpRegistryService registryService;
	private ServiceProviderFileProcessor providerFileProcessor;

	@NotNull
	public String doExecute() throws Exception {

		log.debug("About to execute ProcessRegistryRecordsAction");

		registry = registryService.read(registry.getId());
		if (registry == null) {
			addActionError(getText("error.eirc.invalid_registry_id"));
			return ERROR;
		}

		if (objectIds.isEmpty()) {
			return SUCCESS;
		}

		providerFileProcessor.processRecords(registry, objectIds);

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
		return ERROR;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public SpRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}

	public void setProviderFileProcessor(ServiceProviderFileProcessor providerFileProcessor) {
		this.providerFileProcessor = providerFileProcessor;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}
}
