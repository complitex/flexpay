package org.flexpay.common.service.importexport.imp;

import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.springframework.beans.factory.annotation.Required;

public class ClassToTypeRegistryFactory {

	private static ClassToTypeRegistry registry = null;

	public ClassToTypeRegistry getInstance() {
		return registry;
	}

	@Required
	public void setRegistry(ClassToTypeRegistry registry) {
		ClassToTypeRegistryFactory.registry = registry;
	}
}
