package org.flexpay.common.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.springframework.beans.factory.annotation.Required;

public class ProxyClassToTypeRegistry implements ClassToTypeRegistry {

	private ClassToTypeRegistryFactory factory;

	/**
	 * Get class type id to use in corrections service
	 *
	 * @param clazz Object class
	 * @return Type id
	 */
	@Override
	public int getType(Class<? extends DomainObject> clazz) {
		return factory.getInstance().getType(clazz);
	}

	@Required
	public void setFactory(ClassToTypeRegistryFactory factory) {
		this.factory = factory;
	}
}
