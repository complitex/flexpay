package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;

import java.util.Collections;
import java.util.List;

public class CompositeClassToTypeRegistry implements ClassToTypeRegistry {

	private List<ClassToTypeRegistry> registries = Collections.emptyList();

	/**
	 * Get class type id to use in corrections service
	 *
	 * @param clazz Object class
	 * @return Type id
	 */
	public int getType(Class<? extends DomainObject> clazz) {

		for (ClassToTypeRegistry registry : registries) {
			try {
				return registry.getType(clazz);
			} catch (IllegalArgumentException ex) {
				// do nothing
			}
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}

	public void setRegistries(List<ClassToTypeRegistry> registries) {
		this.registries = registries;
	}
}
