package org.flexpay.common.service;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;

public class PropertiesInitializerHolder<T extends DomainObject> {

	private List<PropertiesInitializer<T>> initializers = CollectionUtils.list();

	public List<PropertiesInitializer<T>> getInitializers() {
		return initializers;
	}

	/**
	 * Add initializer
	 *
	 * @param initializer Initializer to add
	 */
	public void setInitializer(PropertiesInitializer<T> initializer) {
		initializers.add(initializer);
	}
}
