package org.flexpay.common.service;

import org.flexpay.common.persistence.DomainObject;

public class PropertiesInitializerHolderFactory<T extends DomainObject> {

	private final PropertiesInitializerHolder<T> holder = new PropertiesInitializerHolder<T>();

	public PropertiesInitializerHolder<T> getHolder() {
		return holder;
	}
}
