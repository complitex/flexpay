package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.persistence.DomainObject;

public class ClassToTypeRegistryStub implements ClassToTypeRegistry {

	/**
	 * Get class type id to use in corrections service
	 *
	 * @param clazz Object class
	 * @return Type id
	 */
	public int getType(Class<? extends DomainObject> clazz) {

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
