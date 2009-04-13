package org.flexpay.common.persistence.registry;

import org.flexpay.common.persistence.DomainObject;

/**
 * Base class for registry properties
 */
public class RegistryProperties extends DomainObject {

	private Registry registry;

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

}
