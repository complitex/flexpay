package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;

public abstract class ClassToTypeRegistry {

	private List<ClassToTypeRegistry> classes = null;

	/**
	 * Get class type id to use in corrections service
	 *
	 * @param clazz Object class
	 * @return Type id
	 */
	public int getType(Class<? extends DomainObject> clazz) {
		int typeModule = getModuleType(clazz);
		if (checkModuleType(typeModule)) {
			return typeModule;
		}
		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}

	protected int getModuleType(Class<? extends DomainObject> clazz) {

		for (ClassToTypeRegistry classToTypeRegistry : getClasses()) {
			int typeModule = classToTypeRegistry.getModuleType(clazz);
			if (classToTypeRegistry.checkModuleType(typeModule)) {
				return typeModule;
			}
		}
		return getErrorCode();
	}

	protected abstract int getErrorCode();

	public void setClasses(List<ClassToTypeRegistry> cls) {
		classes = cls;
	}

	private boolean checkModuleType(int typeModule) {
		return typeModule != getErrorCode();
	}

	private List<ClassToTypeRegistry> getClasses() {
		if (classes == null) {
			classes = CollectionUtils.list();
		}
		return classes;
	}
}
