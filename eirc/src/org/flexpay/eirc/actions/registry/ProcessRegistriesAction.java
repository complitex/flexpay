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

	public String execute() throws Exception {
		Collection<SpRegistry> registries = registryService.findObjects(objectIds);
		providerFileProcessor.processRegistries(registries);
		return super.execute();
	}

	/**
	 * Getter for property 'townTypeIds'.
	 *
	 * @return Value for property 'townTypeIds'.
	 */
	public Set<Long> getObjectIds() {
		return objectIds;
	}

	/**
	 * Setter for property 'townTypeIds'.
	 *
	 * @param objectIds Value to set for property 'townTypeIds'.
	 */
	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}
}
