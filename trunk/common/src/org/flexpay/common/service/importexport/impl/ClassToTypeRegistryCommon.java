package org.flexpay.common.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;

public class ClassToTypeRegistryCommon implements ClassToTypeRegistry {

	private static final int MODULE_BASE = 0x6000;

    @Override
	public int getType(Class<? extends DomainObject> clazz) {

		if (MeasureUnit.class.isAssignableFrom(clazz)) {
			return MODULE_BASE + 0x09;
		}

		throw new IllegalArgumentException("Class " + clazz + " has no assigned type");
	}
}
