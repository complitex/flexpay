package org.flexpay.bti.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.bti.persistence.building.BuildingAttributeGroup;

public class ClassToTypeRegistryBti implements ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x2000;

	@Override
	public int getType(Class<? extends DomainObject> clazz) {

		if (BuildingAttributeGroup.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x001;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
